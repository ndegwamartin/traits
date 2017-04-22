package com.olympuskid.tr8ts;

import io.fabric.sdk.android.Fabric;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MotionEventCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.plus.PlusShare;
import com.olympuskid.tr8ts.adapters.SharelistAdapter;
import com.olympuskid.tr8ts.asynctasks.DatabaseProgressDialog;
import com.olympuskid.tr8ts.dialog.AlertDialogActivity;
import com.olympuskid.tr8ts.enums.Mode;
import com.olympuskid.tr8ts.listeners.AdmobEventListener;
import com.olympuskid.tr8ts.listeners.NetworkConnectionAvailableListener;
import com.olympuskid.tr8ts.models.GeneratedTextItem;
import com.olympuskid.tr8ts.models.SharelistItem;
import com.olympuskid.tr8ts.utils.Constants;
import com.olympuskid.tr8ts.utils.FontPicker;
import com.olympuskid.tr8ts.utils.FontPicker.FontPickerDialogListener;
import com.olympuskid.tr8ts.utils.ImageUtils;
import com.olympuskid.tr8ts.utils.TraitManager;
import com.olympuskid.tr8ts.utils.TraitManager.Polarity;
import com.olympuskid.tr8ts.utils.TypeFaceHelper;
import com.olympuskid.tr8ts.utils.Utils;
import com.olympuskid.tr8ts.utils.objects.SpicOptions;
import com.olympuskid.tr8ts.utils.objects.ThemeColor;

public class MainActivity extends FragmentActivity implements FontPickerDialogListener, AdmobEventMainActivityInterface {
	EditText captionView;
	TextView resultText;
	ImageButton shareButton;
	TraitManager traitManager;
	String yourName = "";
	AlertDialog.Builder builderChoosePhoto;
	// SocialAuth Component
	SocialAuthAdapter socialAuthAdapter;

	Bitmap processedBitmap = null;
	private Camera camera;
	private int cameraId = 0;
	static ProgressDialog progressBar;
	private LinearLayout statsHolder;
	private static final int REQUEST_CODE_GALLERY_PHOTO = 1;
	private static final int REQUEST_CODE_TAKE_PHOTO = 2;
	private static final int REQUEST_CODE_GPLUS_SHARE = 3;
	public Set<String> deviceGalleryPhotos = new HashSet<String>();
	@SuppressWarnings("rawtypes")
	List mSelectedItems;
	// Create the File where the photo should go
	File photoFile = null;

	private boolean isItalicFont = false;

	private Mode mode;
	private Mode prevMode;

	Uri sourceUri;

	private Button modeButton;
	// image stuff
	ImageButton btnLoadImage;
	TextView textSource;
	EditText editTextCaption;

	TextView statsResultText;
	TextView statsSummaryText;

	ImageView imageResult;
	ImageUtils imageUtils;
	private ImageButton fontPicker;
	private ImageButton settings;
	private ProgressDialog progressDialog;
	private int fontColor = Color.WHITE;
	private int fontShadowColor = Color.BLACK;
	private Typeface fontTypeFace = Typeface.DEFAULT;
	private Integer fontSize;
	private Integer fontTypeFaceStyle = Typeface.NORMAL;
	private boolean isCustomMode = false;

	private AdView adView;
	private NetworkConnectionAvailableListener networkConnectionAvailableListener;
	private GeneratedTextItem generatedTextItem;

	private ImageView saveButton = null;
	private boolean generatedTextItemIsValid = false;
	private AlertDialog shareAlertDialog;

	private static final String TEST_DEVICE_ID = "61941F881247EC098F1C90BEF7BCA030";
	private String characterStat = "";
	private boolean isAPickedFont = false;
	private static final String DEBUG_TAG = "MainActivityDEBUG";

	private ScaleGestureDetector SGD;
	private float scale = 1f;
	private float xOffset = 0f;
	private float yOffset = 0f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.activity_main);
		mode = Mode.NORMAL;
		initialize();

		// Look up the AdView as a resource and load a request.

		adView = (AdView) this.findViewById(R.id.adView);

		// adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice(TEST_DEVICE_ID).build();

		adView.setAdListener(new AdmobEventListener(this));
		adView.loadAd(new AdRequest.Builder().build());
		fontSize = Utils.getInt(Constants.DEFAULT_FONT_SIZE);
		ThemeColor themeColors = TypeFaceHelper
				.getThemeColors(Utils.getInt(Utils.ReadPrefString(this, Constants.KEY_PREF_FONT_THEME, Constants.THEME_DEFAULT)));

		String typeFaceStyle = Utils.ReadPrefString(this, Constants.KEY_PREF_FONT_TYPEFACE_STYLE, Constants.TYPEFACE_STYLE_NORMAL);
		isItalicFont = typeFaceStyle.equals(Constants.TYPEFACE_STYLE_ITALIC) || typeFaceStyle.equals(Constants.TYPEFACE_STYLE_BOLD_ITALIC);

		fontColor = themeColors.getFontColor();
		fontShadowColor = themeColors.getFontShadowColor();
		fontTypeFace = TypeFaceHelper.getTypeFace(Utils.getInt(Utils.ReadPrefString(this, Constants.KEY_PREF_FONT_TYPEFACE, Constants.TYPEFACE_DEFAULT)));
		fontSize = Utils.getInt(Utils.ReadPrefString(this, Constants.KEY_PREF_FONT_SIZE, Constants.DEFAULT_FONT_SIZE).replaceFirst("px", ""));
		fontTypeFaceStyle = TypeFaceHelper.getTypeFaceStyle(Utils.getInt(typeFaceStyle));
		isCustomMode = Utils.ReadPrefBoolean(this, Constants.KEY_PREF_IS_CUSTOM_MODE, false);
		isAPickedFont = Utils.ReadPrefBoolean(this, Constants.MAIN_ACTIVITY_IS_A_PICKED_FONT, false);
		resultText.setTypeface(fontTypeFace, fontTypeFaceStyle);
		resultText.setTextSize(fontSize);
		resultText.setTextColor(fontColor);

		SGD = new ScaleGestureDetector(this, new ScaleListener());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent i = new Intent(this, AlertDialogActivity.class);
		startActivity(i);

		return super.onOptionsItemSelected(item);
	}

	private boolean imgResultScaled = false;
	protected boolean isResizeAction;
	private float fontHorizontalScaleFactor = 1.0f;
	private float fontStrokeWidth = 4.5f;

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			float prevScale = scale;
			scale *= detector.getScaleFactor();
			scale = Math.max(0.1f, Math.min(scale, 1000.0f));
			// imageResult.setBackgroundColor(Color.argb(125, 255, 255, 255));

			Log.d(DEBUG_TAG, "scale value " + scale);

			fontSize = scale > prevScale ? fontSize + Math.round(scale) : fontSize - Math.round(scale);

			Log.d(DEBUG_TAG, "Font Size " + fontSize);

			processTextToImage();
			imgResultScaled = true;

			return true;
		}
	}

	private void initialize() {
		// ScrollView bg = (ScrollView) findViewById(R.id.scroll_bg);
		// Drawable rightArrow = getResources().getDrawable(R.drawable.bgt);

		// setting the opacity (alpha)
		// rightArrow.setAlpha(32);

		// setting the images on the ImageViews
		// bg.setBackgroundDrawable(rightArrow);

		statsResultText = (TextView) findViewById(R.id.statsResultText);
		statsSummaryText = (TextView) findViewById(R.id.statsSummaryText);

		captionView = (EditText) findViewById(R.id.nameText);
		resultText = (TextView) findViewById(R.id.resultText);
		statsHolder = (LinearLayout) findViewById(R.id.statsHolder);
		statsHolder.setVisibility(View.INVISIBLE);
		btnLoadImage = (ImageButton) findViewById(R.id.loadimage);
		// btnProcessing = (Button) findViewById(R.id.processing);
		imageResult = (ImageView) findViewById(R.id.result);
		imageResult.setVisibility(View.GONE);
		fontPicker = (ImageButton) findViewById(R.id.fontButton);

		imageUtils = new ImageUtils(this);

		addGoButtonListener();
		addShareButtonListener();
		attachXbuttonClear();
		addSettingsButtonListener();
		addModeButtonListener();
		addImageResultTouchListner();

		networkConnectionAvailableListener = new NetworkConnectionAvailableListener();

		try {

			traitManager = new TraitManager(getApplicationContext(), true);

			if (traitManager.getTraitsCount() < 1) {

				DatabaseProgressDialog newFragment = new DatabaseProgressDialog();
				newFragment.show(getSupportFragmentManager(), Constants.FRAGMENT_LOAD_DB_CONTENT);

			}

		} catch (Exception e) {

			Log.d("444 ", "On Activity Create");
		}
		// traitManager.clearDb();

		btnLoadImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				builderChoosePhoto.show();
			}
		});

		fontPicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager manager = getSupportFragmentManager();

				FontPicker picker = new FontPicker();
				picker.show(manager, "fontPicker");
			}
		});

		final CharSequence[] items = { getStringResource(R.string.from_gallery), getStringResource(R.string.take_a_picture),
				getStringResource(R.string.no_picture) };
		builderChoosePhoto = new AlertDialog.Builder(this);
		builderChoosePhoto.setTitle(getResources().getString(R.string.choose_a_photo));
		builderChoosePhoto.setIcon(getResources().getDrawable(R.drawable.traitsicon));
		builderChoosePhoto.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int item) {
				if (item == 0) {

					Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, REQUEST_CODE_GALLERY_PHOTO);
				} else if (item == 1) {
					loadDeviceCamera();
				} else {

					sourceUri = null;
					processedBitmap = null;
					resultText.setVisibility(View.VISIBLE);
					imageResult.setVisibility(View.GONE);
					generate();
				}

				return;
			}
		}).create();

		// Adapter initialization
		socialAuthAdapter = new SocialAuthAdapter(new ResponseListener());
	}

	private void addImageResultTouchListner() {
		imageResult.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				final int action = MotionEventCompat.getActionMasked(event);
				float lastXAxis = 0f;
				float lastYAxis = 0f;

				SGD.onTouchEvent(event);

				switch (action) {

				case (MotionEvent.ACTION_DOWN):
					lastXAxis = event.getX();
					lastYAxis = event.getY();
					Log.d(DEBUG_TAG, "Action was DOWN " + lastXAxis + " " + lastYAxis);
					return true;
				case (MotionEvent.ACTION_MOVE):
					isResizeAction = true;
					Log.d(DEBUG_TAG, "Action was MOVE");
					return true;
				case (MotionEvent.ACTION_UP):
					if (!imgResultScaled) {
						xOffset = event.getX() - lastXAxis;
						yOffset = event.getY() - lastYAxis;
					} else {
						imgResultScaled = false;
					}

					processTextToImage();
					Log.d(DEBUG_TAG, "Action was UP " + xOffset + " " + yOffset);
					return true;
				case (MotionEvent.ACTION_CANCEL):
					Log.d(DEBUG_TAG, "Action was CANCEL");
					return true;
				case (MotionEvent.ACTION_OUTSIDE):
					Log.d(DEBUG_TAG, "Movement occurred outside bounds " + "of current screen element");
					return true;
				default:
					return true;
				}
			}
		});

	}

	private String getStringResource(int id) {
		return getResources().getString(id);
	}

	private void addModeButtonListener() {

		final String[] items = { getResources().getString(R.string.normal), getResources().getString(R.string.trash_a_name),
				getResources().getString(R.string.feeling_lucky) };

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.select_mode));
		builder.setIcon(getResources().getDrawable(R.drawable.traitsicon));

		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

				prevMode = mode;

				generate();/*
							 * if (generatedTextItemIsValid) { renderStatsSummary(); } else { statsHolder.setVisibility(View.GONE); }
							 */

			}
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

				mode = prevMode;
			}
		});
		modeButton = (Button) findViewById(R.id.modeButton);
		modeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				prevMode = mode;
				builder.setSingleChoiceItems(items, mode.getValue(), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {
						mode = Mode.getModeFromValue(item);
					}

				});

				builder.create().show();

			}
		});
	}

	private void addSettingsButtonListener() {
		mSelectedItems = new ArrayList(); // Where we track the selected items
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Set the dialog title
		builder.setTitle(getStringResource(R.string.settings))
		// Specify the list array, the items to be selected by default (null for none),
		// and the listener through which to receive callbacks when items are selected

				// Set the action buttons
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// User clicked OK, so save the mSelectedItems results somewhere
						// or return them to the component that opened the dialog

					}
				}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

					}
				});

		settings = (ImageButton) findViewById(R.id.settings);
		final MainActivity that = this;

		settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent i = new Intent(that, AlertDialogActivity.class);
				startActivity(i);

			}
		});
	}

	private void processTextToImage() {
		if (sourceUri != null && resultText.getText() != null && resultText.getText().toString().trim().length() > 0) {

			try {

				boolean isRandomTheme = Utils.ReadPrefString(this, Constants.KEY_PREF_FONT_THEME, Constants.THEME_DEFAULT).equals("100");
				SpicOptions soptions = new SpicOptions();
				soptions.setColor(fontColor);
				soptions.setShadowColor(fontShadowColor);
				soptions.setTypeFace(fontTypeFace);
				soptions.setTextSize(fontSize * 2);
				soptions.setFontItalic(isItalicFont);
				soptions.setIsAPickedFont(isAPickedFont);
				soptions.setXOffset(xOffset);
				soptions.setYOffset(yOffset);
				soptions.setTextHorizontalScaleFactor(fontHorizontalScaleFactor);
				soptions.setStrokeWidth(fontStrokeWidth);

				// random art gen
				soptions = isRandomTheme && !isResizeAction ? ImageUtils.generateTheme(soptions) : soptions;
				// reset main activity vars to new ones
				fontColor = soptions.getColor();
				fontShadowColor = soptions.getShadowColor();
				isItalicFont = soptions.isFontItalic();
				fontTypeFace = soptions.getTypeFace();
				fontStrokeWidth = soptions.getStrokeWidth();
				fontHorizontalScaleFactor = soptions.getTextHorizontalScaleFactor();

				isResizeAction = false;

				processedBitmap = ImageUtils.ProcessBitmap(resultText.getText().toString(), imageUtils.getBitmapFromUri(sourceUri), soptions);
				resultText.setVisibility(View.GONE);
			} catch (IOException e) {
				Log.e("", e.getMessage());
			}
			if (processedBitmap != null) {
				imageResult.setImageBitmap(processedBitmap);
				imageResult.setVisibility(View.VISIBLE);
				statsHolder.setVisibility(View.GONE);
			} else {
				Log.d("processTextToImage()", "Something wrong in processing!");
			}
		} else if (sourceUri != null && resultText.getText() != null && resultText.getText().toString().trim().length() < 1) {
			try {
				imageResult.setImageBitmap(imageUtils.getBitmapFromUri(sourceUri));
				imageResult.setVisibility(View.VISIBLE);
			} catch (IOException e) {
				Log.e("Reinstating photo on rotate", e.getMessage());
			}

		} else {
		}

		yOffset = 0.f;
		xOffset = 0.f;
	}

	private void addGoButtonListener() {

		Button goButton = (Button) findViewById(R.id.goButton);
		goButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				hideKeyboard();
				statsHolder.setVisibility(View.INVISIBLE);
				generate();
			}
		});

		// add listen if user press enter on soft keyboard
		captionView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_CENTER:
					case KeyEvent.KEYCODE_ENTER:
						generate();
						return true;
					default:
						break;
					}
				}
				return false;
			}
		});

	}

	private void addShareButtonListener() {
		shareButton = (ImageButton) findViewById(R.id.shareButton);

		/*
		 * shareButton.setOnTouchListener(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View arg0, MotionEvent me) { if (me.getAction() == MotionEvent.ACTION_DOWN) {
		 * shareButton.setColorFilter(Color.argb(150, 155, 155, 155)); return true; } else if (me.getAction() == MotionEvent.ACTION_UP) {
		 * shareButton.setColorFilter(Color.argb(0, 155, 155, 155)); // or null return true; } return false; } // });
		 */
		final List<SharelistItem> shareListModels = new ArrayList<SharelistItem>();

		SharelistItem facebookModel = new SharelistItem(R.drawable.facebook, "Facebook");
		SharelistItem twitterModel = new SharelistItem(R.drawable.twitter, "Twitter");
		SharelistItem googlePlusModel = new SharelistItem(R.drawable.googleplus, "Google Plus");
		// SharelistItem instagramModel = new SharelistItem(R.drawable.instagram, "Instagram");
		SharelistItem moreModel = new SharelistItem(R.drawable.more_button, getResources().getString(R.string.more));
		SharelistItem saveFileModel = new SharelistItem(R.drawable.save, getResources().getString(R.string.save));

		shareListModels.add(facebookModel);
		shareListModels.add(twitterModel);
		shareListModels.add(googlePlusModel);
		// shareListModels.add(instagramModel);
		shareListModels.add(moreModel);
		shareListModels.add(saveFileModel);
		// our adapter instance
		SharelistAdapter adapter = new SharelistAdapter(this, R.layout.sharelist_row, shareListModels);

		// create a new ListView, set the adapter and item click listener
		final ListView listViewItems = new ListView(this);
		listViewItems.setAdapter(adapter);
		listViewItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (resultText.getText() != null && resultText.getText().toString().trim().length() > 0) {
					String shareText = resultText.getText().toString();

					if (generatedTextItemIsValid) {

						if (position < 4 && !Utils.isNetworkAvailable(MainActivity.this)) {

							Utils.renderFlashMessageLong(MainActivity.this, getStringResource(R.string.no_internet_please));

							return;
						}

						switch (position) {
						case 0:

							showIndeterminateProgressBar(getStringResource(R.string.sharing_on) + " Facebook", getStringResource(R.string.please_wait));

							socialAuthAdapter.addCallBack(Provider.FACEBOOK, Constants.CALLBACK_URL_FB);
							socialAuthAdapter.authorize(MainActivity.this, Provider.FACEBOOK);

							break;
						case 1:
							showIndeterminateProgressBar(getStringResource(R.string.sharing_on) + " Twitter", getStringResource(R.string.please_wait));
							socialAuthAdapter.addCallBack(Provider.TWITTER, Constants.CALLBACK_URL_TWITTER);
							socialAuthAdapter.authorize(MainActivity.this, Provider.TWITTER);

							break;
						case 2:
							// showIndeterminateProgressBar(getStringResource(R.string.sharing_on) + " Google +", getStringResource(R.string.please_wait));
							googlePlusShare();
							shareAlertDialog.dismiss();

							break;/*
								 * case 3: showIndeterminateProgressBar(getStringResource(R.string.sharing_on) + " Instagram",
								 * getStringResource(R.string.please_wait)); socialAuthAdapter.addCallBack(Provider.INSTAGRAM,
								 * Constants.CALLBACK_URL_INSTAGRAM); socialAuthAdapter.authorize(MainActivity.this, Provider.INSTAGRAM);
								 * 
								 * break;
								 */
						case 3:

							LoadSharelistASYNC loadShare = new LoadSharelistASYNC();
							loadShare.execute(new String[] { shareText });

							break;
						case 4:
							if (saveButton != null && saveButton.getTag().equals(1)) {
								Utils.renderFlashMessage(MainActivity.this, getStringResource(R.string.already_saved));
							} else {

								if (processedBitmap != null) {
									SaveImageToDeviceASYNC saveImage = new SaveImageToDeviceASYNC();
									saveImage.execute(new View[] { view });
								} else {

									Utils.renderFlashMessage(MainActivity.this, getStringResource(R.string.please_add_an_image_first));

								}
							}
							break;
						default:
							break;

						}

					} else {

						Utils.renderFlashMessage(MainActivity.this, getStringResource(R.string.enter_name_and_press));
					}
				} else {
					Utils.renderFlashMessage(MainActivity.this, getStringResource(R.string.enter_name_and_press));
				}

			}

		});
		AlertDialog.Builder shareDialogBuilder = new AlertDialog.Builder(MainActivity.this);
		// set title
		shareDialogBuilder.setTitle(getResources().getString(R.string.share));
		// set dialog message
		shareDialogBuilder.setView(listViewItems).setCancelable(false).setIcon(R.drawable.traitsicon)
				.setNegativeButton(getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
						if (saveButton != null) {
							saveButton.setImageResource(R.drawable.save);
							saveButton.setTag(0);
						}
					}
				});

		// create alert dialog
		shareAlertDialog = shareDialogBuilder.create();

		shareButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// show it
				shareAlertDialog.show();

			}
		});
	}

	protected void googlePlusShare() {

		PlusShare.Builder share = new PlusShare.Builder(MainActivity.this);
		if (processedBitmap != null) {

			share.setText(yourName + getStringResource(R.string.means));
			Uri processedUri = imageUtils.getUriFromBitmap(processedBitmap);
			share.addStream(processedUri);
			share.setType(this.getContentResolver().getType(processedUri));
		} else {
			share.setText(resultText.getText().toString());
			share.setType("text/plain");
			share.setContentUrl(Uri.parse(getStringResource(R.string.traits_web_link)));

		}
		try {
			startActivityForResult(share.getIntent(), REQUEST_CODE_GPLUS_SHARE);
		} catch (ActivityNotFoundException e) {
			Utils.renderFlashMessageLong(MainActivity.this, getStringResource(R.string.google_plus_warning));
			dismissProgressDialog();
		}

	}

	private class LoadSharelistASYNC extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			showIndeterminateProgressBar(getStringResource(R.string.loading_share_app_list), getStringResource(R.string.please_wait));

		}

		@Override
		protected String doInBackground(String... params) {

			String shareText = params[0];

			share(shareText);

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			dismissProgressDialog();
		}
	}

	private class SaveImageToDeviceASYNC extends AsyncTask<View, Void, String> {
		View view = null;

		@Override
		protected void onPreExecute() {
			showIndeterminateProgressBar(getStringResource(R.string.saving), getStringResource(R.string.please_wait));

		}

		@Override
		protected String doInBackground(View... params) {

			try {

				view = params[0];
				photoFile = imageUtils.createImageFile();
			} catch (IOException e) {
				Log.e("Creating raw image file", "Raw Image file create failed", e);
			}
			// save to file system
			if (processedBitmap != null && photoFile != null) {

				imageUtils.saveProcessedToSD(processedBitmap, photoFile);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						saveButton = (ImageView) view.findViewById(R.id.shareIcon);
						saveButton.setImageResource(R.drawable.save_disabled);
						saveButton.setTag(1);

					}
				});

			} else {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Utils.renderFlashMessage(MainActivity.this, getStringResource(R.string.something_went_wrong_while));
					}
				});

			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			dismissProgressDialog();
			Utils.renderFlashMessage(MainActivity.this, getStringResource(R.string.saved));
		}
	}

	protected void share(String shareMessage) {
		Intent sharingIntent = null;

		sharingIntent = new Intent(Intent.ACTION_SEND);// create the send intent
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, yourName);

		if (processedBitmap != null) {

			sharingIntent.setType("image/*");
			sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUtils.getUriFromBitmap(processedBitmap));
			sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		} else {

			sharingIntent.setType("text/plain");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
		}
		startActivity(Intent.createChooser(sharingIntent, getStringResource(R.string.share_using)));

		yourName = "";
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_GALLERY_PHOTO:
				sourceUri = data.getData();
				try {
					imageResult.setImageBitmap(imageUtils.getBitmapFromUri(sourceUri));
					imageResult.setVisibility(View.VISIBLE);
				} catch (IOException e) {
					Log.e("Requesting Photo from gallery", e.getMessage());
				}
				break;

			case REQUEST_CODE_TAKE_PHOTO:

				try {
					imageResult.setImageBitmap(imageUtils.getBitmapFromUri(sourceUri));
					imageResult.setVisibility(View.VISIBLE);
					deleteFileSavedToGallery();// Some devices save it to gallery too. snap! we gotta cover that
				} catch (Exception e) {
					Log.e("Taking a photo via camera", e.getMessage());
				}
				break;
			case REQUEST_CODE_GPLUS_SHARE:
				Utils.renderFlashMessage(MainActivity.this, "Shared to Google Plus");
				dismissProgressDialog();
				break;

			default:
				break;
			}

			processTextToImage();
		}
	}

	protected void generate() {
		resultText.setVisibility(View.VISIBLE);
		String textRes = captionView.getText().toString();
		yourName = captionView.getText().toString();

		Polarity polarity = null;
		statsHolder.setVisibility(View.GONE);// start by hiding thesse

		if (mode == Mode.NORMAL) {

			polarity = Polarity.POSITIVE;
			statsHolder.setVisibility(View.GONE);
		} else if (mode == Mode.TRASH_A_NAME) {
			polarity = Polarity.NEGATIVE;

		} else if (mode == Mode.IM_FEELING_LUCKY) {
			polarity = Polarity.ALL;
		} else {
			polarity = Polarity.POSITIVE;
			statsHolder.setVisibility(View.GONE);
		}

		generateRender(textRes, polarity, isCustomMode);

		if (mode == Mode.IM_FEELING_LUCKY && processedBitmap == null)
			renderStatsSummary();

	}

	private void generateRender(String textToRender, Polarity polar, boolean isCustom) {
		resultText.setText(getStringResource(R.string.generating));
		generatedTextItem = traitManager.generate(textToRender, polar, isCustom);
		resultText.setText(generatedTextItem.getGeneratedText());
		generatedTextItemIsValid = generatedTextItem.isValid();
		// captionView.setText("");
		shareButton.setEnabled(true);
		processTextToImage();

	}

	private void showIndeterminateProgressBar(String title, String message) {
		progressDialog = new ProgressDialog(MainActivity.this);
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.show();
	}

	private void addLuckyButtonListener() {
		/*
		 * luckyButton = (Button) findViewById(R.id.luckyButton); luckyButton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View view) { hideKeyboard();
		 * 
		 * if (captionView.getText() != null && captionView.getText().toString().trim().length() > 0) {
		 * 
		 * generateRender(captionView.getText().toString(), Polarity.ALL);
		 * 
		 * renderStatsSummary(); statsHolder.setVisibility(View.VISIBLE);
		 * 
		 * } else { Utils.renderFlashMessage(MainActivity.this,"Enter your name before sharing"); } }
		 * 
		 * });
		 */
	}

	private void renderStatsSummary() {

		if (generatedTextItemIsValid) {

			characterStat = traitManager.getCharacterStat().toString();
			statsResultText.setText(characterStat + "%");
			if (traitManager.getCharacterStat() == 50) {
				statsSummaryText.setText("You are one Lucky person!");
			} else if (traitManager.getCharacterStat() == 100) {
				statsSummaryText.setText("You are on top of the World! \n :-) :-) :-)");
			} else if (traitManager.getCharacterStat() == 0) {
				statsSummaryText.setText("Whoa! I really think you need some help \n :-D :-D :-D");
			} else {
				statsSummaryText.setText(generateTraitRatingFeedback(traitManager.getCharacterStat() > 50));
			}
			statsHolder.setVisibility(View.VISIBLE);
		}
	}

	public String generateTraitRatingFeedback(boolean isGoodAverage) {

		String emoticon = !isGoodAverage ? "\n :-P " : "\n :-)";

		String verb = traitManager.getVerb(isGoodAverage);
		List<String> pronouns = traitManager.getPronouns();
		List<String> nouns = traitManager.getNouns();

		boolean stateFlag = Utils.getRandomNumbersInRange(0, 1) == 0 ? true : false;// to show person and articles
		if (!stateFlag) {
			if (!pronouns.contains("so")) {

				pronouns.add("so");
			}
		}

		String noun = nouns.get(Utils.getRandomNumbersInRange(0, nouns.size() - 1)) + " ";
		String pronoun = pronouns.get(Utils.getRandomNumbersInRange(0, pronouns.size() - 1));

		String definiteArticle = Utils.characterIsVowel(pronoun.substring(0, 1)) ? "an " : "a ";
		pronoun = pronoun.trim().equals("so") && definiteArticle.trim().equals("a") ? "very" : pronoun;

		return "You are " + (stateFlag ? definiteArticle : "") + pronoun + " " + verb.toLowerCase(Locale.US) + " " + (stateFlag ? noun : "") + emoticon;

	}

	private void hideKeyboard() {
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(captionView.getWindowToken(), 0);

	}

	private void attachXbuttonClear() {

		final Drawable x = getResources().getDrawable(R.drawable.clearpurple);// your x image
		x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());
		captionView.setCompoundDrawables(captionView.getText().toString().equals("") ? null : x, null, null, null);
		captionView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (captionView.getCompoundDrawables()[0] == null) {
					return false;
				}
				if (event.getAction() != MotionEvent.ACTION_UP) {
					return false;
				}
				if (event.getX() < (captionView.getPaddingLeft() + x.getIntrinsicWidth())) {
					captionView.setText("");
					captionView.setCompoundDrawables(null, null, null, null);
				}
				return false;
			}
		});
		captionView.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				captionView.setCompoundDrawables(captionView.getText().toString().equals("") ? null : x, null, null, null);
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
		});

	}

	@Override
	public void onFontSelected(FontPicker fontPicker) {
		// Toast.makeText(this, "Yatta! Picked " + fontPicker.getSelectedFont().getTypefaceStyle(), Toast.LENGTH_LONG).show();

		resultText.setTypeface(fontPicker.getSelectedFont().getTypeface(), fontPicker.getSelectedFont().getTypefaceStyle());
		fontTypeFace = fontPicker.getSelectedFont().getTypeface();
		fontTypeFaceStyle = fontPicker.getSelectedFont().getTypefaceStyle();
		isItalicFont = fontPicker.getSelectedFont().isItalic();
		isAPickedFont = true;
		isResizeAction = true;// hack-fix for Random Art gen halt
		processTextToImage();
	}

	protected void takeAPhoto() {
		if (camera != null) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (intent.resolveActivity(getPackageManager()) != null) {

				try {
					photoFile = imageUtils.createImageFile();
				} catch (IOException ex) {
					Log.e("", "Error trying to create file... SD card maybe???", ex);
				}

				// Continue only if the File was successfully created
				if (photoFile != null) {
					// Keep a list for afterwards
					populatePhotoList();

					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
					sourceUri = Uri.fromFile(photoFile);
					startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
				}

			}

		} else {
			Log.d("", "Camera returned null...");
		}
	}

	private void openCamera() {
		try {
			if (!imageUtils.phoneHasCamera()) {
				imageUtils.showAlert("No cameras on this device");
			} else {
				cameraId = imageUtils.findHindFacingCamera();
				if (cameraId < 0) {
					imageUtils.showAlert(getStringResource(R.string.no_camera_found));
				} else {
					camera = Camera.open(cameraId);
				}
			}
		} catch (Exception exception) {
			Log.i("Opening Camera: ", exception.getMessage());
		}

	}

	@Override
	protected void onResume() {

		callSuperOnResume();
		if (adView != null) {
			adView.resume();
		}

		this.registerReceiver(networkConnectionAvailableListener, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
	}

	private void callSuperOnResume() {

		super.onResume();

	}

	@Override
	protected void onPause() {

		if (camera != null) {
			camera.release();
			camera = null;
		}

		if (adView != null) {
			adView.pause();
		}

		this.unregisterReceiver(networkConnectionAvailableListener);

		super.onPause();
	}

	@Override
	public void onDestroy() {
		dismissProgressDialog();

		if (adView != null) {
			adView.destroy();
		}

		try {
			traitManager.closeDB();
		} catch (Exception e) {
			Log.d("444", "On Activity Destroy");
		}

		super.onDestroy();
	}

	// Lets handle auto restore of state e.g. on Autorotation
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		savedInstanceState.putString(Constants.MAIN_ACTIVITY_RESULT_TEXT, resultText.getText().toString());
		savedInstanceState.putInt(Constants.MAIN_ACTIVITY_TYPEFACE, TypeFaceHelper.getTypeFace(fontTypeFace));
		savedInstanceState.putInt(Constants.MAIN_ACTIVITY_TYPEFACESTYLE, fontTypeFaceStyle);
		savedInstanceState.putBoolean(Constants.MAIN_ACTIVITY_IS_ITALIC, isItalicFont);
		savedInstanceState.putBoolean(Constants.MAIN_ACTIVITY_IS_A_PICKED_FONT, isAPickedFont);
		savedInstanceState.putBoolean(Constants.MAIN_ACTIVITY_GENERATED_TEXT_VALID, generatedTextItemIsValid);
		savedInstanceState.putInt(Constants.MAIN_ACTIVITY_GEN_MODE, mode.getValue());
		savedInstanceState.putString(Constants.MAIN_ACTIVITY_STATS_SUMMARY, statsSummaryText.getText().toString());
		savedInstanceState.putString(Constants.MAIN_ACTIVITY_CHARACTER_STAT, characterStat);
		savedInstanceState.putString(Constants.MAIN_ACTIVITY_YOUR_NAME, yourName);
		savedInstanceState.putFloat(Constants.MAIN_ACTIVITY_FONT_STROKE_WIDTH, fontStrokeWidth);
		savedInstanceState.putFloat(Constants.MAIN_ACTIVITY_FONT_HORIZONTAL_SCALE_FACTOR, fontHorizontalScaleFactor);
		savedInstanceState.putInt(Constants.MAIN_ACTIVITY_FONT_COLOR, fontColor);
		savedInstanceState.putInt(Constants.MAIN_ACTIVITY_FONT_SHADOW_COLOR, fontShadowColor);
		// savedInstanceState.putFloat(Constants.MAIN_ACTIVITY_X_OFFSET, xOffset);
		// savedInstanceState.putFloat(Constants.MAIN_ACTIVITY_Y_OFFSET, yOffset);
		// hack/fix to halt random art on rotate
		isResizeAction = true;
		savedInstanceState.putBoolean(Constants.MAIN_ACTIVITY_IS_RESIZE_ACTION, isResizeAction);
		if (sourceUri != null)
			savedInstanceState.putString(Constants.MAIN_ACTIVITY_SOURCE_URI, sourceUri.toString());

		super.onSaveInstanceState(savedInstanceState);
	}

	// called after onStart()
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		resultText.setText(savedInstanceState.getString(Constants.MAIN_ACTIVITY_RESULT_TEXT));
		yourName = savedInstanceState.getString(Constants.MAIN_ACTIVITY_YOUR_NAME);
		isItalicFont = savedInstanceState.getBoolean(Constants.MAIN_ACTIVITY_IS_ITALIC);
		isAPickedFont = savedInstanceState.getBoolean(Constants.MAIN_ACTIVITY_IS_A_PICKED_FONT);
		fontTypeFaceStyle = savedInstanceState.getInt(Constants.MAIN_ACTIVITY_TYPEFACESTYLE);
		fontTypeFace = TypeFaceHelper.getTypeFace(savedInstanceState.getInt(Constants.MAIN_ACTIVITY_TYPEFACE));
		fontStrokeWidth = savedInstanceState.getFloat(Constants.MAIN_ACTIVITY_FONT_STROKE_WIDTH);
		fontHorizontalScaleFactor = savedInstanceState.getFloat(Constants.MAIN_ACTIVITY_FONT_HORIZONTAL_SCALE_FACTOR);
		isResizeAction = savedInstanceState.getBoolean(Constants.MAIN_ACTIVITY_IS_RESIZE_ACTION);
		fontColor = savedInstanceState.getInt(Constants.MAIN_ACTIVITY_FONT_COLOR);
		fontShadowColor = savedInstanceState.getInt(Constants.MAIN_ACTIVITY_FONT_SHADOW_COLOR);
		if (savedInstanceState.getString(Constants.MAIN_ACTIVITY_SOURCE_URI) != null)
			sourceUri = Uri.parse(savedInstanceState.getString(Constants.MAIN_ACTIVITY_SOURCE_URI));
		resultText.setTypeface(fontTypeFace, fontTypeFaceStyle);
		generatedTextItemIsValid = savedInstanceState.getBoolean(Constants.MAIN_ACTIVITY_GENERATED_TEXT_VALID);
		mode = Mode.getModeFromValue(savedInstanceState.getInt(Constants.MAIN_ACTIVITY_GEN_MODE));
		if (mode == Mode.IM_FEELING_LUCKY) {
			characterStat = savedInstanceState.getString(Constants.MAIN_ACTIVITY_CHARACTER_STAT);
			statsSummaryText.setText(savedInstanceState.getString(Constants.MAIN_ACTIVITY_STATS_SUMMARY));
			statsResultText.setText(characterStat + "%");
			statsHolder.setVisibility(View.VISIBLE);
		}
		// xOffset = savedInstanceState.getFloat(Constants.MAIN_ACTIVITY_X_OFFSET);
		// yOffset = savedInstanceState.getFloat(Constants.MAIN_ACTIVITY_Y_OFFSET);

		processTextToImage();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//
		String key = intent.getStringExtra("sharedPref");
		MainActivity.this.onSharedPreferenceChanged(PreferenceManager.getDefaultSharedPreferences(this), key);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(Constants.KEY_PREF_FONT_SIZE)) {
			fontSize = Utils.getInt(sharedPreferences.getString(Constants.KEY_PREF_FONT_SIZE, Constants.DEFAULT_FONT_SIZE).replaceFirst("px", ""));

		} else if (key.equals(Constants.KEY_PREF_FONT_THEME)) {

			ThemeColor themeColors = TypeFaceHelper.getThemeColors(Utils.getInt(sharedPreferences.getString(Constants.KEY_PREF_FONT_THEME,
					Constants.THEME_DEFAULT)));

			fontColor = themeColors.getFontColor();
			fontShadowColor = themeColors.getFontShadowColor();

		} else if (key.equals(Constants.KEY_PREF_FONT_TYPEFACE_STYLE)) {

			String typeFaceStyle = sharedPreferences.getString(Constants.KEY_PREF_FONT_TYPEFACE_STYLE, Constants.TYPEFACE_STYLE_NORMAL);
			isItalicFont = typeFaceStyle.equals(Constants.TYPEFACE_STYLE_ITALIC) || typeFaceStyle.equals(Constants.TYPEFACE_STYLE_BOLD_ITALIC);

			fontTypeFaceStyle = TypeFaceHelper.getTypeFaceStyle(Utils.getInt(typeFaceStyle));

		} else if (key.equals(Constants.KEY_PREF_FONT_TYPEFACE)) {

			fontTypeFace = TypeFaceHelper.getTypeFace(Utils.getInt(sharedPreferences.getString(Constants.KEY_PREF_FONT_TYPEFACE, Constants.TYPEFACE_DEFAULT)));
		} else if (key.equals(Constants.KEY_PREF_IS_CUSTOM_MODE)) {

			isCustomMode = sharedPreferences.getBoolean(key, false);
		}
		resultText.setTypeface(fontTypeFace, fontTypeFaceStyle);
		resultText.setTextSize(fontSize);
		resultText.setTextColor(fontColor);
		if (key.equals(Constants.KEY_PREF_IS_CUSTOM_MODE)) {
			generate();
		} else {
			processTextToImage();
		}

	}

	@Override
	public void renderAdView(boolean render) {
		if (adView != null)
			adView.setVisibility(render && AdmobEventListener.isAdLoaded ? View.VISIBLE : View.GONE);

	}

	private void populatePhotoList() {
		// initialize the list!
		deviceGalleryPhotos.clear();
		String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME };
		// intialize the Uri and the Cursor, and the current expected size.
		Cursor c = null;
		Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		//
		// Query the Uri to get the data path. Only if the Uri is valid.
		if (u != null) {
			c = getContentResolver().query(u, projection, null, null, null);
		}

		// If we found the cursor and found a record in it (we also have the id).
		if ((c != null) && (c.moveToFirst())) {
			do {
				// Loop each and add to the list.
				deviceGalleryPhotos.add(c.getString(0));
			} while (c.moveToNext());
		}
	}

	private void deleteFileSavedToGallery() {

		String[] projection = { MediaStore.Images.ImageColumns.SIZE, MediaStore.Images.ImageColumns.DISPLAY_NAME, MediaStore.Images.ImageColumns.DATA,
				BaseColumns._ID, };
		//
		// intialize the Uri and the Cursor, and the current expected size.
		Cursor c = null;
		Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

		// Query the Uri to get the data path. Only if the Uri is valid

		if ((u != null)) {
			c = getContentResolver().query(u, projection, null, null, null);
		}
		//
		// If we found the cursor and found a record in it (we also have the size).
		if ((c != null) && (c.moveToFirst())) {
			do {
				// Check each if image is in gallery list we created earlier.
				boolean bFound = false;

				if (deviceGalleryPhotos.contains(c.getString(1))) {

					bFound = true;

				}

				// To here we looped the full gallery.
				if (!bFound) {
					imageUtils.deleteImageFromGallery(BaseColumns._ID + "=" + c.getString(3));

					break;
				}
			} while (c.moveToNext());
		}

	}

	private void dismissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	private final class ResponseListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {
			Log.d("ShareButton", "Authentication Successful");

			// Get name of provider after authentication
			final String providerName = values.getString(SocialAuthAdapter.PROVIDER);
			Log.d("ShareButton", "Provider Name = " + providerName);

			// Please avoid sending duplicate message. Social Media Providers
			// block duplicate messages.

			try {
				photoFile = imageUtils.createImageFile();
			} catch (IOException e) {
				Log.e("ResponseListener:: ImageUtils.CreateImageFile", e.getMessage());
			}

			if (processedBitmap != null && photoFile != null) {
				imageUtils.saveProcessedToSD(processedBitmap, photoFile);

				try {
					socialAuthAdapter.uploadImageAsync(yourName + getStringResource(R.string.means), imageUtils.generateFileName(), processedBitmap, 100,
							new UploadImageListener());
				} catch (Exception e) {
					Log.e("ResponseListener:: socialAuthAdapter.uploadImageAsync", e.getMessage());
					dismissProgressDialog();
				}
			} else {

				socialAuthAdapter.updateStatus(resultText.getText().toString(), new MessageListener(), false);

			}

			Utils.renderFlashMessage(MainActivity.this, "Shared via " + providerName);
			shareAlertDialog.dismiss();
			dismissProgressDialog();
		}

		@Override
		public void onError(SocialAuthError error) {
			dismissProgressDialog();
			Log.d("ShareButton", "Authentication Error: " + error.getMessage());
		}

		@Override
		public void onCancel() {
			dismissProgressDialog();
			Log.d("ShareButton", "Authentication Cancelled");
		}

		@Override
		public void onBack() {
			dismissProgressDialog();
			Log.d("Share-Button", "Dialog Closed by pressing Back Key");
		}

	}

	// To get status of image upload after authentication
	private final class UploadImageListener implements SocialAuthListener<Integer> {

		@Override
		public void onExecute(String provider, Integer status) {
			// mDialog.dismiss();

			if (status.intValue() == 200 || status.intValue() == 201 || status.intValue() == 204) {
			}
			// Toast.makeText(, "Image Uploaded", Toast.LENGTH_SHORT).show();
			// else
			// Toast.makeText(CustomUI.this, "Image not Uploaded", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(SocialAuthError e) {

		}
	}

	// To get status of message after authentication
	private final class MessageListener implements SocialAuthListener<Integer> {
		@Override
		public void onExecute(String provider, Integer t) {
			/*
			 * Integer status = t; if (status.intValue() == 200 || status.intValue() == 201 || status.intValue() == 204)
			 * Utils.renderFlashMessage(MainActivity.this,getStringResource(R.string.message_posted_on) + providers); else
			 * Utils.renderFlashMessage(MainActivity.this,getStringResource(R.string.message_not_posted_on) + provider);
			 */
		}

		@Override
		public void onError(SocialAuthError e) {

		}
	}

	private void loadDeviceCamera() {

		if (camera == null) {

			AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

				@Override
				protected void onPreExecute() {
					showIndeterminateProgressBar(getStringResource(R.string.loading), getStringResource(R.string.please_wait));
				}

				@Override
				protected Void doInBackground(Void... arg0) {
					openCamera();

					return null;
				}

				@Override
				protected void onPostExecute(Void result) {

					takeAPhoto();
					dismissProgressDialog();
				}

			};
			task.execute((Void[]) null);
		}
	}

	@Override
	public void onBackPressed() {
		if (shareAlertDialog != null && shareAlertDialog.isShowing())
			shareAlertDialog.dismiss();
		// hideShare AlertDialog
		super.onBackPressed(); // optional depending on your needs
	}

}
