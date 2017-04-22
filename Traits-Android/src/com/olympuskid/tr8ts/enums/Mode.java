package com.olympuskid.tr8ts.enums;

public enum Mode {
	NORMAL(0), TRASH_A_NAME(1), IM_FEELING_LUCKY(2);
	private final int code;

	Mode(int i) {
		code = i;
	}

	public int getValue() {
		return code;
	}

	@SuppressWarnings("static-access")
	public static Mode getModeFromValue(int code) {

		for (Mode m : Mode.values()) {
			if (code == m.NORMAL.getValue()) {
				return m.NORMAL;

			} else if (code == m.TRASH_A_NAME.getValue()) {
				return m.TRASH_A_NAME;

			} else if (code == m.IM_FEELING_LUCKY.getValue()) {
				return m.IM_FEELING_LUCKY;

			}
		}
		return null;
	}

}
