package com.huangsz.recorder.data;

import android.net.Uri;

public interface ProviderConstants {

    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = "com.huangsz.recorder";

    /**
     * Base URI. (content://com.huangsz.recorder)
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
}
