package com.xin.lv.yang.utils.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机application相关操作
 */

public class AppUtils {
    private static AppUtils appUtils;

    public static synchronized AppUtils getInstance() {
        if (appUtils == null) {
            appUtils = new AppUtils();
        }
        return appUtils;
    }


    public String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;

    }

    public String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();
        return imsi;
    }


    public static String MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];

        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }


    public List<Integer> getWindowWAndH(Activity a) {
        List<Integer> wh = new ArrayList<>();
        DisplayMetrics metric = new DisplayMetrics();
        a.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        wh.add(width);
        wh.add(height);
        return wh;
    }


    public long stringToLong(String time) {
        long longTime = 0;
        if (time != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.US);
            try {
                Date date = formatter.parse(time);
                longTime = date.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return longTime;
    }


    public static final String[] PHONES_PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};


    /**
     * 获取手机联系人
     *
     * @param context
     */
    public List<HashMap<String, String>> getPhoneContacts(Context context) {
        ContentResolver resolver = context.getContentResolver();
        List<HashMap<String, String>> list = new ArrayList<>();
        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                HashMap<String, String> map = new HashMap<>();
                //得到手机号码
                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(PHONES_PROJECTION[1]));
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber)) continue;
                //得到联系人名称
                String contactName = phoneCursor.getString(phoneCursor.getColumnIndex(PHONES_PROJECTION[0]));
                map.put("phone", phoneNumber);
                map.put("name", contactName);
                list.add(map);
                map = null;
            }
            phoneCursor.close();
        }
        return list;
    }


    /**
     * 得到手机SIM卡联系人人信息
     **/
    public List<HashMap<String, String>> getSIMContacts(Context context) {
        ContentResolver resolver = context.getContentResolver();
        List<HashMap<String, String>> list = new ArrayList<>();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                HashMap<String, String> map = new HashMap<>();
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(PHONES_PROJECTION[1]));
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber)) continue;
                // 得到联系人名称
                String contactName = phoneCursor.getString(phoneCursor.getColumnIndex(PHONES_PROJECTION[0]));
                map.put("phone", phoneNumber);
                map.put("name", contactName);
                list.add(map);
                map = null;
            }

            phoneCursor.close();
        }

        return list;
    }


    /**
     * 得到手机联系人的集合信息
     **/

    public ArrayList<HashMap<String, String>> readContact(Context context) {

        // 首先,从raw_contacts中读取联系人的id("contact_id")
        // 其次, 根据contact_id从data表中查询出相应的电话号码和联系人名称
        // 然后,根据mimetype来区分哪个是联系人,哪个是电话号码

        Uri rawContactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");

        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        // 从raw_contacts中读取所有联系人的id("contact_id")
        Cursor rawContactsCursor = context.getContentResolver().query(rawContactsUri, new String[]{"contact_id"}, null, null, null);
        if (rawContactsCursor != null) {
            while (rawContactsCursor.moveToNext()) {
                String contactId = rawContactsCursor.getString(0);
                // 根据contact_id从data表中查询出相应的电话号码和联系人名称, 实际上查询的是视图view_data
                Cursor dataCursor = context.getContentResolver().query(dataUri, new String[]{"data1", "mimetype"}, "contact_id=?", new String[]{contactId}, null);

                if (dataCursor != null) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    while (dataCursor.moveToNext()) {
                        String data1 = dataCursor.getString(0);
                        String mimetype = dataCursor.getString(1);

                        if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                            // 手机号码
                            map.put("phone", data1);
                        } else if ("vnd.android.cursor.item/name".equals(mimetype)) {
                            // 联系人名字
                            map.put("name", data1);
                        }
                    }
                    list.add(map);
                    dataCursor.close();
                }
            }
            rawContactsCursor.close();
        }
        return list;
    }


    /**
     * 判断是否为电话号码
     *
     * @param mobiles 电话号码
     * @return 是否为正确的电话号码
     */
    public static boolean isMobileNumber(String mobiles) {

        String str = "1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}";

        if (mobiles.length() == 11) {
            Pattern p = Pattern.compile(str);

            Matcher m = p.matcher(mobiles);
            return m.matches();

        } else {
            return false;
        }
    }


    public static String conversionTime(String time) {
        if (!time.equals("null")) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日", Locale.US);
            long lt = Long.valueOf(time + "000");
            Date date = new Date(lt);
            String str = f.format(date);
            return str;
        }
        return "";
    }


    /**
     * 解析HTML代码
     *
     * @param str html代码的字符串
     */
    public static String showHtml(String str) {
        // 解析HTML字符串返回一个Document实现
        Document doc = Jsoup.parse(str);
        Elements links = doc.getElementsByTag("p");
        if (links.size() != 0) {
            Element l = links.get(0);
            String link = l.select("span").text();
            return link;
        }
        return "";
    }


    /**
     * 解析html中的图片
     *
     * @param str
     * @return
     */
    public static List<String> htmlImage(String str) {
        List<String> list = new ArrayList<>();
        Document doc = Jsoup.parse(str);
        Elements links = doc.getElementsByTag("p");
        for (Element t : links) {
            String s = t.select("img ").attr("src").trim();
            list.add(s);
        }
        return list;
    }

}
