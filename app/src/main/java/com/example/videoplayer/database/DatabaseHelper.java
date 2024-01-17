package com.example.videoplayer.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.videoplayer.model.Playlist;
import com.example.videoplayer.model.Video;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "videoplayer.db";
    public static final int DATABASE_VERSION = 2;

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public class PlaylistTable {
        public static final String TABLE_NAME = "playlist";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME + " TEXT)";
    }

    public class VideoTable {
        public static final String TABLE_NAME = "video";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ID_FILE = "id_file";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PATH = "path";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_THUMBNAILURI = "thumbnailUri";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_ID_FILE + " TEXT," +
                        COLUMN_TITLE + " TEXT," +
                        COLUMN_PATH + " TEXT," +
                        COLUMN_DURATION + " TEXT," +
                        COLUMN_SIZE + " TEXT," +
                        COLUMN_THUMBNAILURI + " TEXT)";
    }


    public class Playlist_Video {
        public static final String TABLE_NAME = "playlist_video";
        public static final String COLUMN_PLAYLIST_ID = "playlist_id";
        public static final String COLUMN_VIDEO_ID = "video_id";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "("
                        + COLUMN_PLAYLIST_ID + " INTERGER,"
                        + COLUMN_VIDEO_ID + " INTERGER,"
                        + "FOREIGN KEY (" + COLUMN_PLAYLIST_ID + ") REFERENCES " + PlaylistTable.TABLE_NAME + "(" + PlaylistTable.COLUMN_ID + "),"
                        + "FOREIGN KEY (" + COLUMN_VIDEO_ID + ") REFERENCES " + VideoTable.TABLE_NAME + "(" + VideoTable.COLUMN_ID + "),"
                        + "PRIMARY KEY (" + COLUMN_PLAYLIST_ID + ", " + COLUMN_VIDEO_ID + ")"
                        + ")";
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PlaylistTable.CREATE_TABLE);
        db.execSQL(VideoTable.CREATE_TABLE);
        db.execSQL(Playlist_Video.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +PlaylistTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +VideoTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +Playlist_Video.TABLE_NAME);
        db.execSQL(PlaylistTable.CREATE_TABLE);
        db.execSQL(VideoTable.CREATE_TABLE);
        db.execSQL(Playlist_Video.CREATE_TABLE);
    }

    public ArrayList<Playlist> getAllPlaylists(Context context) {
        ArrayList<Playlist> playlists = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM playlist";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String playlistId = cursor.getString(cursor.getColumnIndexOrThrow(PlaylistTable.COLUMN_ID));
                String playlistName = cursor.getString(cursor.getColumnIndexOrThrow(PlaylistTable.COLUMN_NAME));
                Playlist playlist = new Playlist(playlistName, playlistId);
                playlists.add(playlist);
            } while (cursor.moveToNext());
        }
        cursor.close();
//        db.close();
        return playlists;
    }

    public void addVideoToTable(String id_file, String title, String path, String duration, String size, String thumbnailUri) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            if (!isVideoExists(path)) {
                ContentValues values = new ContentValues();
                values.put(VideoTable.COLUMN_ID_FILE, id_file);
                values.put(VideoTable.COLUMN_TITLE, title);
                values.put(VideoTable.COLUMN_PATH, path);
                values.put(VideoTable.COLUMN_DURATION, duration);
                values.put(VideoTable.COLUMN_SIZE, size);
                values.put(VideoTable.COLUMN_THUMBNAILURI, thumbnailUri);
                db.insert(VideoTable.TABLE_NAME, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isVideoExists(String path) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + VideoTable.TABLE_NAME +
                " WHERE " + VideoTable.COLUMN_PATH + "=?",new String[]{path});
        boolean exists = cursor.moveToFirst();
        cursor.close();
//        db.close();
        return exists;
    }

    public void addVideoToPlaylist(Context context, String videoTitle, String playlistName) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = -1;
        Cursor videoCursor = db.rawQuery("SELECT " + VideoTable.COLUMN_ID +
                " FROM " + VideoTable.TABLE_NAME +
                " WHERE " + VideoTable.COLUMN_TITLE + "=?", new String[]{videoTitle});

        if (videoCursor.moveToFirst()) {
            int videoId = videoCursor.getInt(videoCursor.getColumnIndexOrThrow(VideoTable.COLUMN_ID));
            Cursor playlistCursor = db.rawQuery("SELECT " + PlaylistTable.COLUMN_ID +
                    " FROM " + PlaylistTable.TABLE_NAME +
                    " WHERE " + PlaylistTable.COLUMN_NAME + "=?", new String[]{playlistName});

            if (playlistCursor.moveToFirst()) {
                int playlistId = playlistCursor.getInt(playlistCursor.getColumnIndexOrThrow(PlaylistTable.COLUMN_ID));
                ContentValues values = new ContentValues();
                values.put(Playlist_Video.COLUMN_PLAYLIST_ID, playlistId);
                values.put(Playlist_Video.COLUMN_VIDEO_ID, videoId);
                result = db.insert(Playlist_Video.TABLE_NAME, null, values);
            }
            playlistCursor.close();
        }

        videoCursor.close();
        if (result !=-1){
            Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Video đã tồn tại", Toast.LENGTH_SHORT).show();
        }
//        db.close();
    }

    public ArrayList<Video> getPlaylist(String playlistName) {
        ArrayList<Video> videos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor playlistCursor = db.rawQuery("SELECT " + PlaylistTable.COLUMN_ID +
                " FROM " + PlaylistTable.TABLE_NAME +
                " WHERE " + PlaylistTable.COLUMN_NAME + "=?", new String[]{playlistName});

        if (playlistCursor.moveToFirst()) {
            int playlistId = playlistCursor.getInt(playlistCursor.getColumnIndexOrThrow(PlaylistTable.COLUMN_ID));

            String query = "SELECT * FROM " + Playlist_Video.TABLE_NAME +
                    " WHERE " + Playlist_Video.COLUMN_PLAYLIST_ID + "=?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(playlistId)});

            while (cursor.moveToNext()) {
                int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(Playlist_Video.COLUMN_VIDEO_ID));

                String videoQuery = "SELECT * FROM " + VideoTable.TABLE_NAME +
                        " WHERE " + VideoTable.COLUMN_ID + "=?";
                Cursor videoCursor = db.rawQuery(videoQuery, new String[]{String.valueOf(videoId)});

                if (videoCursor.moveToFirst()) {
                    String id_file = videoCursor.getString(videoCursor.getColumnIndexOrThrow(VideoTable.COLUMN_ID_FILE));
                    long id_file_long = Long.parseLong(id_file);
                    Uri thumbnailuri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id_file_long);
                    String title = videoCursor.getString(videoCursor.getColumnIndexOrThrow(VideoTable.COLUMN_TITLE));
                    String path = videoCursor.getString(videoCursor.getColumnIndexOrThrow(VideoTable.COLUMN_PATH));
                    String duration = videoCursor.getString(videoCursor.getColumnIndexOrThrow(VideoTable.COLUMN_DURATION));
                    String size = videoCursor.getString(videoCursor.getColumnIndexOrThrow(VideoTable.COLUMN_SIZE));
                    String thumbnailUri = thumbnailuri.toString();
                    Video video = new Video(id_file, title, path, duration, size, thumbnailUri);
                    videos.add(video);
                }
                videoCursor.close();
            }
            cursor.close();
        }
        playlistCursor.close();

        return videos;
    }
    public void deletePlaylist(String playlistID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + PlaylistTable.TABLE_NAME + " WHERE " + PlaylistTable.COLUMN_ID + " = '" + playlistID + "'");
    }

    public void renamePlaylist(String playlistID, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + PlaylistTable.TABLE_NAME +
                " SET " + PlaylistTable.COLUMN_NAME + " = '" + newName + "'" +
                " WHERE " + PlaylistTable.COLUMN_ID + " = '" + playlistID + "'");
    }






}
