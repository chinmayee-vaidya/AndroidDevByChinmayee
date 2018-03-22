package com.example.chinmayee.allthingssweet.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Chinmayee on 3/8/18.
 */

public class RecepieContract {
    public class RecepiesColumns{
        public static final String RECEPIE_ID= "rec_id";
        public static final String RECEPIE_NAME = "rec_name";
        public static final String REC_SERV_SIZE = "rec_serv_size";
        public static final String REC_IMAGE ="rec_image";
        public static final String IS_WID ="is_wid";
        public final String[] REC_COLUMNS={RECEPIE_ID,RECEPIE_NAME,REC_SERV_SIZE,REC_IMAGE,IS_WID};
    }

    public class IngredientsColumns{
        public static final String ING_QTY = "ingredient_qty";
        public static final String ING_MEASURE ="ing_measure";
        public static final String ING_NAME ="ing_name";
        public static final String ING_REC_ID ="ing_rec_id";
        public String[] ING_COLUMNS ={ING_QTY,ING_MEASURE,ING_NAME,ING_REC_ID};
    }

    public class StepColumns{
        public static final String STEP_ID="step_id";
        public static final String SHORT_DESC = "step_short_desc";
        public static final String STEP_DESC="step_desc";
        public static final String VIDEO_URL="video_url";
        public static final String VIDEO_THUM_URL="thumb_url";
        public static final String STEP_REC_ID = "step_rec_id";
        public String[] STEP_COLS = {STEP_ID, SHORT_DESC,STEP_DESC,VIDEO_URL,VIDEO_THUM_URL,STEP_REC_ID};
    }

    public static final String BASE_URI = "com.example.chinmayee.allthingssweet.data";

    public static final Uri BASE_CONTENT_URI  =Uri.parse("content://"+BASE_URI);
    public static final String REC_SUFFLIX = "recepies";
    public static final String ING_SUFFLIX ="ingredients";
    public static final String STEP_SUFFLIX ="steps";
    public static final String WIDGET_SUFFLIX="widget";

    public static final class  RecepiesEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(REC_SUFFLIX)
                .build();

        /**
         *
         * @param  id of the movie to be fetched
         * @return single movie details based on the movie ID
         */
        public static Uri buildRecUriWithID(String id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }

        public static Uri getWidgetUri(){
            return CONTENT_URI.buildUpon()
                    .appendPath(WIDGET_SUFFLIX)
                    .build();
        }

    }

    public static final class IngredientsEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(ING_SUFFLIX)
                .build();
        /**
         *
         * @param  id of the movie to be fetched
         * @return single movie details based on the movie ID
         */
        public static Uri buildRecUriWithID(String id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }
    }

    public static final class StepsEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(STEP_SUFFLIX)
                .build();
        /**
         *
         * @param  id of the movie to be fetched
         * @return single movie details based on the movie ID
         */
        public static Uri buildRecUriWithID(String id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }
    }
}
