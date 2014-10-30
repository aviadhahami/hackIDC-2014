package com.idc.easycheck.check;

import java.security.InvalidKeyException;

import android.util.SparseArray;

public class Checks {
	static private SparseArray<Check> checks = new SparseArray<Check>();
	static private int currentCheckID = 0;
	
	public static int addCheck(Check check) {
		checks.put(currentCheckID, check);
		currentCheckID++;
		return currentCheckID;
	}
	
	public static Check getCheck(int checkID) throws InvalidKeyException {
		Check ret = checks.get(checkID);
		if (null == ret) {
			throw new InvalidKeyException(String.format("%d", checkID));
		}
		return ret;
	}
}