package com.olympuskid.tr8ts.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import com.olympuskid.tr8ts.utils.objects.ColorHolder;
import com.olympuskid.tr8ts.utils.objects.Dimension;
import com.olympuskid.tr8ts.utils.objects.Font;
import com.olympuskid.tr8ts.utils.objects.SpicOptions;

public class ImageUtils {
	private static Context ctx;
	private final int IMAGE_MAX_SIZE;
	private static String captionString = "";
	private static Bitmap originalBitmap;
	private static Paint paintText;
	private static Paint strokePaint;
	static Random random;
	List<Integer> typeFaceStyle = new ArrayList<Integer>();
	List<Typeface> typeFaces = new ArrayList<Typeface>();
	final static List<Font> fonts = new ArrayList<Font>();

	public ImageUtils(Context context) {

		ctx = context;
		IMAGE_MAX_SIZE = getMaxImageSizePossible(context);

		paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
		strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		random = new Random();
		typeFaceStyle.add(Typeface.NORMAL);
		typeFaceStyle.add(Typeface.ITALIC);
		typeFaceStyle.add(Typeface.BOLD);
		typeFaceStyle.add(Typeface.BOLD_ITALIC);

		typeFaces.add(Typeface.DEFAULT_BOLD);
		typeFaces.add(Typeface.DEFAULT);
		typeFaces.add(Typeface.MONOSPACE);
		typeFaces.add(Typeface.SANS_SERIF);
		typeFaces.add(Typeface.SERIF);

		// add fonts to List
		Font font;
		for (int tfStyle : typeFaceStyle) {
			for (Typeface tFace : typeFaces) {

				font = new Font(tFace, tfStyle);

				fonts.add(font);

			}
		}

	}

	public static Bitmap ProcessBitmap(String captionString, Bitmap originalBitmap, SpicOptions soptions) {

		Bitmap newBitmap = null;
		ImageUtils.captionString = captionString;
		ImageUtils.originalBitmap = originalBitmap;

		try {

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;

			Config config = originalBitmap.getConfig();
			if (config == null) {
				config = Bitmap.Config.ARGB_8888;
			}

			newBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), config);
			Canvas newCanvas = new Canvas(newBitmap);

			newCanvas.drawBitmap(originalBitmap, 0, 0, null);

			paintText.setColor(soptions.getColor());
			paintText.setTextSize(soptions.getTextSize());
			paintText.setStyle(soptions.getStyle());
			paintText.setTextSkewX(soptions.isFontItalic() ? soptions.getTextSkewX() : 0f);
			paintText.setTextScaleX(soptions.getTextHorizontalScaleFactor());
			paintText.setTypeface(soptions.getTypeFace());
			paintText.setShadowLayer(12f, 10f, 10f, soptions.getShadowColor());

			// stroke
			strokePaint.setTextSize(soptions.getTextSize());
			strokePaint.setColor(soptions.getShadowColor());
			strokePaint.setStyle(Paint.Style.STROKE);
			strokePaint.setTextSkewX(paintText.getTextSkewX());
			strokePaint.setTextScaleX(paintText.getTextScaleX());
			strokePaint.setStrokeWidth(soptions.getStrokeWidth());
			strokePaint.setTypeface(soptions.getTypeFace());

			Rect bounds = new Rect();

			paintText.getTextBounds(captionString, 0, captionString.length(), bounds);

			String[] textArray = captionString.split("\\r?\\n");
			float unit = soptions.getTextSize();
			for (int x = 0; x < textArray.length; x++) {
				newCanvas.drawText(textArray[x], soptions.getXOffset() + 10, soptions.getYOffset() + bounds.height() + (x * (5 + unit)), strokePaint);
				newCanvas.drawText(textArray[x], soptions.getXOffset() + 10, soptions.getYOffset() + bounds.height() + (x * (5 + unit)), paintText);
			}

		} catch (Exception e) {
			Log.d("", "e.getMessage()");
		}

		return newBitmap;
	}

	public static boolean getRandomBoolean() {
		return random.nextBoolean();
	}

	public static float getRandomFloatFromRange(float minX, float maxX) {
		return random.nextFloat() * (maxX - minX) + minX;
	}

	public static int generateRandomColor(ColorHolder mix) {

		int alpha = random.nextInt(256);
		int red = random.nextInt(256);
		int green = random.nextInt(256);
		int blue = random.nextInt(256);

		if (getRandomBoolean()) {
			// mix the color
			if (mix != null) {
				red = ((red + mix.red) * 10) / 18;
				green = ((green + mix.green) * 10) / 18;
				blue = ((blue + mix.blue) * 10) / 18;
			}
		}

		boolean useAlphaGen = random.nextInt(2) == 1 ? true : false;// ration 2:1 i.e. use alpha with a skewed 1/3 chance
		if (useAlphaGen) {
			return Color.argb(alpha, red, green, blue);
		} else {
			return Color.rgb(red, green, blue);
		}
	}

	public static SpicOptions generateTheme(SpicOptions sOptions) {
		ColorHolder colorHolder = new ColorHolder();
		colorHolder.red = random.nextInt(256);
		colorHolder.green = random.nextInt(256);
		colorHolder.blue = random.nextInt(256);

		sOptions.setColor(Color.rgb(colorHolder.red, colorHolder.green, colorHolder.blue));

		sOptions.setShadowColor(generateRandomColor(colorHolder));
		if (!sOptions.isAPickedFont()) {
			Font selectedFont = fonts.get(Utils.getRandomNumbersInRange(0, (fonts.size() - 1)));
			sOptions.setFontItalic(selectedFont.isItalic());
			sOptions.setTypeFace(selectedFont.getTypeface());
		}
		sOptions.setStrokeWidth(Utils.getFloat(Utils.getRandomNumbersInRange(3, 9)));
		sOptions.setTextHorizontalScaleFactor(getRandomFloatFromRange(0.7f, 1.2f));
		return sOptions;
	}

	public Bitmap refresh(SpicOptions options) {
		return ProcessBitmap(ImageUtils.captionString, ImageUtils.originalBitmap, options);

	}

	public Uri saveToSD(Bitmap bitmap) {
		File pictureFile = null;

		try {
			File rootFolder = Environment.getExternalStorageDirectory();
			if (rootFolder.canWrite()) {
				pictureFile = createImageFile();
				FileOutputStream out = new FileOutputStream(pictureFile);
				bitmap.compress(CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
			}

			ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		} catch (IOException e) {
			Log.d("", "Could not save image to file system ... " + e.getMessage());
		}

		return Uri.fromFile(pictureFile);
	}

	public Uri saveProcessedToSD(Bitmap bitmap, File pictureFile) {

		try {
			File rootFolder = Environment.getExternalStorageDirectory();
			if (rootFolder.canWrite()) {
				FileOutputStream out = new FileOutputStream(pictureFile);
				bitmap.compress(CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
			}

			// applicationContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
			addImageToGallery(pictureFile.getAbsolutePath(), ctx);
			// cleanUpOriginalImage();
		} catch (IOException e) {
			Log.d("", "Could not save image to file system ... " + e.getMessage());
		}

		return Uri.fromFile(pictureFile);
	}

	public void addImageToGallery(final String filePath, final Context context) {

		ContentValues values = new ContentValues();

		values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
		values.put(Images.Media.MIME_TYPE, "image/png");
		values.put(MediaStore.MediaColumns.DATA, filePath);

		context.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
	}

	public void deleteImageFromGallery(String filePath) {

		ContentResolver cr = ctx.getContentResolver();
		cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePath, null);
	}

	public String generateFileName() {
		Time now = new Time();
		now.setToNow();
		return "Traits_" + now.toString() + ".png";
	}

	public File createImageFile() throws IOException {

		// Create an image file name
		String timeStamp = Utils.formatedDate("yyyyMMdd_HHmmss");
		String imageFileName = "Traits_" + timeStamp + "_";

		File root = Environment.getExternalStorageDirectory();

		File storageDir = new File(root + "/" + Constants.IMAGE_FOLDER);

		if (!storageDir.exists() || !storageDir.isDirectory()) {
			storageDir = new File(root + "/" + Constants.IMAGE_FOLDER + "/");
			storageDir.mkdirs();

		}

		// File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

		File image = File.createTempFile(imageFileName, /* prefix */
				".png", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		// maybe if other app to display it
		// currentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

	private Bitmap loadImageFromFilePath(String filepath, Integer scale) {
		/*
		 * BitmapFactory.Options options = new BitmapFactory.Options(); options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		 * 
		 * if (scale != null) { // options.inSampleSize = scale; } // options.inSampleSize = 2; options.inPurgeable = true;
		 * 
		 * Bitmap bitmap = BitmapFactory.decodeFile(filepath, options); return bitmap;
		 */
		return retrieveBitmap(Uri.fromFile(new File(filepath)), 600, 600);

	}

	private Dimension getBitmapDimensions(String mCurrentPhotoPath) {

		// Get the dimensions of the bitmap by decode
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		return new Dimension(bmOptions.outWidth, bmOptions.outHeight);

	}

	public Bitmap cropBitmap(Bitmap bitmap) {

		Matrix matrix = new Matrix();
		matrix.postScale(0.5f, 0.5f);
		return Bitmap.createBitmap(bitmap, 100, 100, 100, 100, matrix, true);
	}

	public int findFrontFacingCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				Log.d("", "Face Camera Found ... ");
				cameraId = i;
				break;
			}
		}
		return cameraId;
	}

	public int findHindFacingCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
				Log.d("", "Back Camera Found ... ");
				cameraId = i;
				break;
			}
		}
		return cameraId;
	}

	// Read bitmap
	public Bitmap readBitmap(Uri selectedImage) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 5;// 20% size reduce
		AssetFileDescriptor fileDescriptor = null;
		try {
			fileDescriptor = ctx.getContentResolver().openAssetFileDescriptor(selectedImage, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
				fileDescriptor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	// Clear bitmap

	public void clearBitmap(Bitmap bm) {

		bm.recycle();

		System.gc();

	}

	public boolean phoneHasCamera() {
		return ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	public void showAlert(String message) {

		Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
	}

	public Bitmap getImageAsBitmap(String filepath) {
		Bitmap bitmap = null;

		try {
			Dimension imageDimensions = getBitmapDimensions(filepath);

			int scale = 1;
			if (imageDimensions.getHeight() > IMAGE_MAX_SIZE || imageDimensions.getWidth() > IMAGE_MAX_SIZE) {
				scale = (int) Math
						.pow(2,
								(int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(imageDimensions.getHeight(), imageDimensions.getWidth()))
										/ Math.log(0.5)));
			}

			// Decode with inSampleSize
			bitmap = loadImageFromFilePath(filepath, scale);

			return bitmap;

		} catch (Exception e) {
			Log.e("", e.getMessage());
			return null;

		}
	}

	private int getMaxImageSizePossible(Context context) {
		final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		final int height = displayMetrics.heightPixels;
		final int width = displayMetrics.widthPixels;

		// Use the longest side to resize our images.
		return Math.max(height, width);
	}

	// we should have the images available in gallery too
	public void galleryAddPic(String filePath) {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(filePath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		ctx.sendBroadcast(mediaScanIntent);
	}

	// render same orientation as display
	public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
		CameraInfo info = new CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}

	public static Bitmap retrieveBitmap(Uri fileUri, int width, int height) {

		int imageExifOrientation = 0;
		// Samsung Galaxy Note 2 and S III doesn't return the image in the correct orientation, therefore rotate it based on the data held in the exif.

		try {

			ExifInterface exif;
			exif = new ExifInterface(fileUri.getPath());
			imageExifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		int rotationAmount = 0;

		if (imageExifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			// Need to do some rotating here...
			rotationAmount = 270;
		}
		if (imageExifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			// Need to do some rotating here...
			rotationAmount = 90;
		}
		if (imageExifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			// Need to do some rotating here...
			rotationAmount = 180;
		}

		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(fileUri.getPath(), bmOptions);
		int photoWidth = bmOptions.outWidth;
		int photoHeight = bmOptions.outHeight;

		// Needs to be at least 1, or a a power of two for scaling down
		int scaleFactor = Math.max(1, Math.min(photoWidth / width, photoHeight / height));

		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap scaledDownBitmap = BitmapFactory.decodeFile(fileUri.getPath(), bmOptions);

		if (rotationAmount != 0) {
			Matrix mat = new Matrix();
			mat.postRotate(rotationAmount);
			scaledDownBitmap = Bitmap.createBitmap(scaledDownBitmap, 0, 0, scaledDownBitmap.getWidth(), scaledDownBitmap.getHeight(), mat, true);
		}
		return scaledDownBitmap;

	}

	public Bitmap getBitmapFromUri(Uri uri) throws IOException {
		int imageExifOrientation = 0;
		// Rotate after checking data contained in exif.

		try {

			ExifInterface exif;
			exif = new ExifInterface(uri.getPath());
			imageExifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		int rotationAmount = 0;

		if (imageExifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			// Need to do some rotating here...
			rotationAmount = 270;
		}
		if (imageExifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			// Need to do some rotating here...
			rotationAmount = 90;
		}
		if (imageExifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			// Need to do some rotating here...
			rotationAmount = 180;
		}

		ParcelFileDescriptor parcelFileDescriptor = ctx.getContentResolver().openFileDescriptor(uri, "r");
		FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(fileDescriptor, null, bmOptions);
		Dimension imageDimensions = new Dimension(bmOptions.outWidth, bmOptions.outHeight);

		int scaleFactor = 1;
		if (imageDimensions.getHeight() > IMAGE_MAX_SIZE || imageDimensions.getWidth() > IMAGE_MAX_SIZE) {
			scaleFactor = (int) Math.pow(2,
					(int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(imageDimensions.getHeight(), imageDimensions.getWidth())) / Math.log(0.5)));
		}

		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, bmOptions);

		if (rotationAmount != 0) {
			Matrix mat = new Matrix();
			mat.postRotate(rotationAmount);
			image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), mat, true);
		}

		parcelFileDescriptor.close();
		return image;
	}

	public Uri getUriFromBitmap(Bitmap image) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, bytes);
		String path = Images.Media.insertImage(ctx.getContentResolver(), image, "Title", null);
		return Uri.parse(path);
	}
}
