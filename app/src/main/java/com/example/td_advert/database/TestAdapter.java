package com.example.td_advert.database;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.td_advert.bean.AnalyticEvent;
import com.example.td_advert.bean.AppState;
import com.example.td_advert.bean.Company;
import com.example.td_advert.bean.Company.TabConfig;
import com.example.td_advert.bean.Feedback;
import com.example.td_advert.bean.MessageBean;
import com.example.td_advert.bean.Station;
import com.example.td_advert.bean.UserEmail;
import com.google.gson.Gson;

public class TestAdapter {
	protected static final String TAG = "DataAdapter";

	private final Context mContext;
	private SQLiteDatabase mDb;
	private DataBaseHelper mDbHelper;

	public TestAdapter(Context context) {
		this.mContext = context;
		mDbHelper = new DataBaseHelper(mContext);
	}

	public TestAdapter createDatabase() throws SQLException {
		try {
			mDbHelper.createDataBase();
		} catch (IOException mIOException) {
			Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
			throw new Error("UnableToCreateDatabase");
		}
		return this;
	}

	public TestAdapter open() throws SQLException {
		try {
			mDbHelper.openDataBase();
			mDbHelper.close();
			mDb = mDbHelper.getReadableDatabase();
		} catch (SQLException mSQLException) {
			Log.e(TAG, "open >>" + mSQLException.toString());
			throw mSQLException;
		}
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public Cursor getTestData() {
		try {
			String sql = "SELECT * FROM myTable";

			Cursor mCur = mDb.rawQuery(sql, null);
			if (mCur != null) {
				mCur.moveToNext();
			}
			return mCur;
		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public boolean SaveCompanyDetails(String name, String logo,
			String backgroundImage, String VersionNumber) {
		try {
			System.out.println("Values>>>>>>>>>>>>>>>>>>>" + name + " " + logo
					+ " " + backgroundImage);
			mDb = mDbHelper.getWritableDatabase();

			ContentValues cv = new ContentValues();
			cv.put("Name", name);
			cv.put("BackgroundImage", backgroundImage);
			cv.put("Logo", logo);
			cv.put("VersionNumber", VersionNumber);
			System.out.println("ContentValues>>>>>>>>>" + cv);
			mDb.insert("Company", null, cv);

			Log.d("SaveCompanyDetails", "informationsaved");
			return true;

		} catch (Exception ex) {
			Log.d("SaveCompanyDetails", ex.toString());
			return false;
		}
	}

	public boolean UpdateCompanyDetails(String name, String logo,
			String backgroundImage, Integer CompanyId, String VersionNumber) {
		try {
			System.out.println("UpdateValues>>>>>>>>>>>>>>>>>>>" + name + " "
					+ logo + " " + backgroundImage);
			mDb = mDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("Name", name);
			cv.put("BackgroundImage", backgroundImage);
			cv.put("Logo", logo);
			cv.put("VersionNumber", VersionNumber);
			System.out.println("ContentValues>>>>>>>>>" + cv);
			// mDb.insert("Company", null, cv);
			int count = mDb.update("Company", cv, "Id=" + CompanyId, null);
			if (count == 0) {
				SaveCompanyDetails(name, logo, backgroundImage, VersionNumber);
			}
			Log.d("SaveCompanyDetails", "informationsaved");
			return true;

		} catch (Exception ex) {
			Log.d("SaveCompanyDetails", ex.toString());
			return false;
		}
	}

	public boolean SaveCompany(Company company) {
		try {
			mDb = mDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("Name", company.getName());
			cv.put("BackgroundImage", company.getBackground_image());
			cv.put("Logo", company.getLogo());
			cv.put("VersionNumber", company.getVersion());
			cv.put("zip_path", company.getVersion());

			int count = mDb.update("Company", cv,
					"companyId=" + company.getCompanyId(), null);
			if (count == 0) {
				cv.put("companyId", company.getCompanyId());
				mDb.insert("Company", null, cv);
			}

			SaveTabConfiguration(company.getCompanyId(), company.getTabs());
			return true;

		} catch (Exception ex) {
			Log.d("SaveCompanyDetails", ex.toString());
			return false;
		}
	}

	public boolean SaveFeedback(Feedback feedback) {
		try {
			mDb = mDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();

			cv.put("feedback_type", feedback.getFeedback_type());
			if (feedback.getFeedback_type() != null
					&& feedback.getFeedback_type().equalsIgnoreCase("MAGAZINE")) {
				cv.put("name", feedback.getMagazineName());
			} else {
				cv.put("name", feedback.getName());
			}
			cv.put("number", feedback.getPhoneNumber());
			cv.put("message", feedback.getFeedbackText());
			cv.put("rating", feedback.getRating());
			mDb.insert("Feedback", null, cv);

			return true;

		} catch (Exception ex) {
			Log.d("SaveFeedback", ex.toString());
			return false;
		}
	}

    public boolean SaveMessage(MessageBean msg){
        try {
            mDb = mDbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put("message", msg.getMessage().toString());
            mDb.insert("MessageSync", null, cv);
            return true;
        } catch (Exception ex) {
            Log.d("Save Message", ex.toString());
            return false;
        }
    }

	public void removeAllFeedbacks() {
		String sql = "Delete From Feedback";
		mDb.execSQL(sql);
	}

    public boolean SaveTaxiNo(String taxiNo){
        try {
            mDb = mDbHelper.getWritableDatabase();
            //String sql = "UPDATE TabletInformation SET taxiNo = '" + taxiNo + "'";
            //mDb.rawQuery(sql, null);
            ContentValues cv = new ContentValues();
            cv.put("taxiNo", taxiNo);
            mDb.update("TabletInformation", cv, "", null);

            return true;
        } catch (Exception ex) {
            Log.d("Save Tablet Information", ex.toString());
            return false;
        }
    }

    public boolean SaveTestDevice(Integer isTest){
        try {
            mDb = mDbHelper.getWritableDatabase();
            //String sql = "UPDATE TabletInformation SET taxiNo = '" + taxiNo + "'";
            //mDb.rawQuery(sql, null);
            ContentValues cv = new ContentValues();
            cv.put("testDevice", isTest);
            mDb.update("TabletInformation", cv, "", null);

            return true;
        } catch (Exception ex) {
            Log.d("Save Test Device", ex.toString());
            return false;
        }
    }

    public boolean SaveTaxiStationID(Integer id){
        try {
            mDb = mDbHelper.getWritableDatabase();
            //String sql = "UPDATE TabletInformation SET taxiNo = '" + taxiNo + "'";
            //mDb.rawQuery(sql, null);
            ContentValues cv = new ContentValues();
            cv.put("taxiStationID", id);
            mDb.update("TabletInformation", cv, "", null);

            return true;
        } catch (Exception ex) {
            Log.d("Save taxiStationID", ex.toString());
            return false;
        }
    }

    public String getTaxiNo(){
        String taxiNo = null;
        try {
            mDb = mDbHelper.getWritableDatabase();
            String sql = "SELECT * FROM TabletInformation";
            Cursor mCur = mDb.rawQuery(sql, null);

            if(mCur.getCount() > 0) {
                mCur.moveToFirst();
                taxiNo = mCur.getString(mCur.getColumnIndex("taxiNo"));
            } else {
                taxiNo = "";
            }

        } catch(Exception ex) {
            Log.d("get Taxi No", ex.toString());
        }

        return taxiNo;
    }

    public Integer getTaxiStationID(){
        Integer stationID = 0;
        try {
            mDb = mDbHelper.getWritableDatabase();
            String sql = "SELECT * FROM TabletInformation";
            Cursor mCur = mDb.rawQuery(sql, null);

            if(mCur.getCount() > 0) {
                mCur.moveToFirst();
                stationID = mCur.getInt(mCur.getColumnIndex("taxiStationID"));
            } else {
                stationID = 0;
            }

        } catch(Exception ex) {
            Log.d("Get TestDevice Status", ex.toString());
        }

        return stationID;
    }

    public Integer getUpdateStatus(){
        Integer updateStatus = 0;
        try {
            mDb = mDbHelper.getWritableDatabase();
            String sql = "SELECT * FROM TabletInformation";
            Cursor mCur = mDb.rawQuery(sql, null);

            if(mCur.getCount() > 0) {
                mCur.moveToFirst();
                updateStatus = mCur.getInt(mCur.getColumnIndex("hasUpdate"));
            } else {
                updateStatus = 0;
            }

        } catch(Exception ex) {
            Log.d("Get updateStatus Status", ex.toString());
        }

        return updateStatus;
    }

    public Integer getTestDeviceStatus(){
        Integer testDevice = 0;
        try {
            mDb = mDbHelper.getWritableDatabase();
            String sql = "SELECT * FROM TabletInformation";
            Cursor mCur = mDb.rawQuery(sql, null);

            if(mCur.getCount() > 0) {
                mCur.moveToFirst();
                testDevice = mCur.getInt(mCur.getColumnIndex("testDevice"));
            } else {
                testDevice = 0;
            }

        } catch(Exception ex) {
            Log.d("get TestDevice Status", ex.toString());
        }

        return testDevice;
    }

	public void removeTaxi() {
		String sql = "Delete From " + DataBaseHelper.TABLE_EVENT;
		mDb.execSQL(sql);
	}

    public JSONArray getMessages(){
        JSONArray msgs = null;
        try {
            msgs = new JSONArray();
            mDb = mDbHelper.getWritableDatabase();

            String sql = "SELECT * FROM MessageSync";
            Cursor mCur = mDb.rawQuery(sql, null);

            if (mCur.getCount() > 0) {
                mCur.moveToFirst();
                do {
                    /*MessageBean messageBean = new MessageBean();
                    try {
                        Log.d("Zenyai", mCur.getString(mCur.getColumnIndex("message")));
                        messageBean.setMessage(new JSONObject(mCur.getString(mCur.getColumnIndex("message"))));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/

                    msgs.put(new JSONObject(mCur.getString(mCur.getColumnIndex("message"))));

                } while (mCur.moveToNext());
            }

        } catch (Exception ex){
            Log.d("getMessages", ex.toString());
        }

        return msgs;
    }

	public ArrayList<Feedback> getFeedbacks() {
		ArrayList<Feedback> feedbacks = null;
		try {
			feedbacks = new ArrayList<Feedback>();
			mDb = mDbHelper.getWritableDatabase();

			String sql = "SELECT * FROm Feedback";
			Cursor mCur = mDb.rawQuery(sql, null);

			if (mCur.getCount() > 0) {
				mCur.moveToFirst();
				do {
					Feedback feedback = new Feedback();
					feedback.setFeedback_type(mCur.getString(mCur
							.getColumnIndex("feedback_type")));
					if (feedback.getFeedback_type() != null
							&& feedback.getFeedback_type().equalsIgnoreCase(
									"MAGAZINE")) {
						feedback.setMagazineName(mCur.getString(mCur
								.getColumnIndex("name")));
					} else {
						feedback.setName(mCur.getString(mCur
								.getColumnIndex("name")));
					}
					feedback.setPhoneNumber(mCur.getString(mCur
							.getColumnIndex("number")));
					feedback.setFeedbackText(mCur.getString(mCur
							.getColumnIndex("message")));
					feedback.setRating(mCur.getInt(mCur
							.getColumnIndex("rating")));

					feedbacks.add(feedback);

				} while (mCur.moveToNext());
			}
		} catch (Exception ex) {
			Log.d("getFeedbacks", ex.toString());
		}

		return feedbacks;
	}

    public ArrayList<Station> getStationNames() {
        ArrayList<Station> names = null;
        try {
            names = new ArrayList<Station>();
            mDb = mDbHelper.getWritableDatabase();

            String sql = "SELECT * FROm TaxiStation";
            Cursor mCur = mDb.rawQuery(sql, null);

            if (mCur.getCount() > 0) {
                mCur.moveToFirst();
                do {
                    Station sta = new Station();
                    sta.setId(mCur.getInt(mCur.getColumnIndex("id")));
                    sta.setStationName(mCur.getString(mCur.getColumnIndex("stationName")));
                    names.add(sta);

                } while (mCur.moveToNext());
            }
        } catch (Exception ex) {
            Log.d("getStationNames", ex.toString());
        }

        return names;
    }

	public boolean saveUserEmail(UserEmail userEmail) {
		try {
			mDb = mDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			// CREATE TABLE "UserEmails" ("email" TEXT, "company_name" TEXT)
			cv.put("company_name", userEmail.getCompanyName());
			cv.put("email", userEmail.getEmailAddress());

			mDb.insert("UserEmails", null, cv);

			return true;

		} catch (Exception ex) {
			Log.d("saveUserEmail", ex.toString());
			return false;
		}
	}

    public boolean saveTaxiStation(Integer id, String name){
        try {
            mDb = mDbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("id", id);
            cv.put("stationName", name);

            mDb.insert("TaxiStation", null, cv);

            return true;

        } catch (Exception ex) {
            Log.d("saveTaxiStation", ex.toString());
            return false;
        }
    }

    public void removeAllStationName() {
        String sql = "Delete From TaxiStation";
        mDb.execSQL(sql);
    }

	public void removeAllUserEmails() {
		String sql = "Delete From UserEmails";
		mDb.execSQL(sql);
	}

	public ArrayList<UserEmail> getUserEmails() {
		ArrayList<UserEmail> userEmails = null;
		try {
			userEmails = new ArrayList<UserEmail>();
			mDb = mDbHelper.getWritableDatabase();

			String sql = "SELECT * FROm UserEmails";
			Cursor mCur = mDb.rawQuery(sql, null);

			if (mCur.getCount() > 0) {
				mCur.moveToFirst();
				do {
					UserEmail userEmail = new UserEmail();
					userEmail.setCompanyName(mCur.getString(mCur
							.getColumnIndex("company_name")));
					userEmail.setEmailAddress(mCur.getString(mCur
							.getColumnIndex("email")));

					userEmails.add(userEmail);

				} while (mCur.moveToNext());
			}
		} catch (Exception ex) {
			Log.d("getUserEmails", ex.toString());
		}

		return userEmails;
	}

	// CREATE TABLE "Exception_Logs" ("id" INTEGER PRIMARY KEY AUTOINCREMENT NOT
	// NULL , "exception_log" TEXT)
	public boolean saveExceptionLog(String exceptionLog) {
		try {
			mDb = mDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			// CREATE TABLE "UserEmails" ("email" TEXT, "company_name" TEXT)
			cv.put("exception_log", exceptionLog);

			mDb.insert("Exception_Logs", null, cv);

			return true;

		} catch (Exception ex) {
			Log.d("saveExceptionLog", ex.toString());
			return false;
		}
	}

	public void removeAllExceptionLogs() {
		String sql = "Delete From Exception_Logs";
		mDb.execSQL(sql);
	}

    public void removeAllMessages() {
        String sql = "Delete From MessageSync";
        mDb.execSQL(sql);
    }

	public ArrayList<String> getExceptionLogs() {
		ArrayList<String> exceptionLogs = null;
		try {
			exceptionLogs = new ArrayList<String>();
			mDb = mDbHelper.getWritableDatabase();

			String sql = "SELECT * FROm Exception_Logs";
			Cursor mCur = mDb.rawQuery(sql, null);

			if (mCur.getCount() > 0) {
				mCur.moveToFirst();
				do {

					exceptionLogs.add(mCur.getString(mCur
							.getColumnIndex("exception_log")));
					break;
				} while (mCur.moveToNext());
			}
		} catch (Exception ex) {
			Log.d("getExceptionLogs", ex.toString());
		}

		return exceptionLogs;
	}

	public void populateState() {
		try {
			mDb = mDbHelper.getWritableDatabase();
			AppState.getInstance().clearCompanies();
			String sql = "SELECT * FROm Company";
			Cursor mCur = mDb.rawQuery(sql, null);

			if (mCur.getCount() > 0) {
				mCur.moveToFirst();
				do {
					Company company = new Company();
					company.setCompanyId(mCur.getInt(mCur.getColumnIndex("companyId")));
					company.setName(mCur.getString(mCur.getColumnIndex("Name")));
					company.setVersion(mCur.getInt(mCur.getColumnIndex("VersionNumber")));
					company.setBackground_image(mCur.getString(mCur.getColumnIndex("BackgroundImage")));
					company.setCompany_path(mCur.getString(mCur.getColumnIndex("zip_path")));
					company.setLogo(mCur.getString(mCur.getColumnIndex("Logo")));
					company.setTabs(findTabConfiguration(company.getCompanyId()));

					AppState.getInstance().addCompany(company);
				} while (mCur.moveToNext());
			}
		} catch (Exception ex) {
			Log.d("SaveCompanyDetails", ex.toString());
		}
	}

	public void removeNotRequiredCompanies(String companyIds) {
		// int i = mDb.delete("Company", "companyId not in ("+companyIds+")",
		// null);
		// Log.i("", ""+i);
		String sql = "Delete FROm Company where companyId not in ("
				+ companyIds + ")";
		int deletedCount = mDb.delete("Company", "companyId not in ("
				+ companyIds + ")", null);

		Log.e("Removal", "" + deletedCount);
		// mDb.rawQuery(sql, null);
	}

    public void removeCompany(String companyId){
        mDb = mDbHelper.getWritableDatabase();
        mDb.execSQL("DELETE FROM Company where companyId="+ companyId );
        Log.v("Zenyai", "got call " + companyId);
    }

	public Company findCompany(int id) {
		Company company = null;
		try {
			company = new Company();
			mDb = mDbHelper.getWritableDatabase();

			String sql = "SELECT * FROm Company WHERE companyId=" + id;
			Cursor mCur = mDb.rawQuery(sql, null);

			if (mCur.getCount() > 0) {
				mCur.moveToFirst();
				company.setCompanyId(mCur.getInt(mCur
						.getColumnIndex("companyId")));
				company.setName(mCur.getString(mCur.getColumnIndex("Name")));
				company.setVersion(mCur.getInt(mCur
						.getColumnIndex("VersionNumber")));
				company.setBackground_image(mCur.getString(mCur
						.getColumnIndex("BackgroundImage")));
//				company.setZip_path(mCur.getString(mCur
//						.getColumnIndex("zip_path")));
				company.setLogo(mCur.getString(mCur.getColumnIndex("Logo")));

				company.setTabs(findTabConfiguration(id));
			}
		} catch (Exception ex) {
			Log.d("SaveCompanyDetails", ex.toString());
		}

		return company;
	}

	public boolean SaveTabConfiguration(int companyId, TabConfig config) {
		try {
			mDb = mDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("homepage_title", config.getHomepage_title());
			cv.put("homepage_background", config.getHomepage_background());
			cv.put("layout1_title", config.getLayout1_title());
			cv.put("layout1_background", config.getLayout1_background());
			cv.put("layout2_title", config.getLayout2_title());
			cv.put("layout2_background", config.getLayout2_background());
			cv.put("layout3_title", config.getLayout3_title());
			cv.put("layout3_background", config.getLayout3_background());
			cv.put("video_title", config.getVideo_title());
			cv.put("video_bg", config.getVideo_bg());
			cv.put("video", config.getVideo());
			cv.put("video_thumb", config.getVideo_thumb());
			cv.put("video_text", config.getVideo_text());
			cv.put("promotion_popup", config.getPromotion_popup());
            cv.put("placeholder", config.getPlaceholder());

			int count = mDb.update("TabConfig", cv, "companyId=" + companyId,
					null);
			if (count == 0) {
				cv.put("companyId", companyId);
				mDb.insert("TabConfig", null, cv);
			}

			return true;

		} catch (Exception ex) {
			Log.d("SaveCompanyDetails", ex.toString());
			return false;
		}
	}

	public TabConfig findTabConfiguration(int companyId) {
		TabConfig config = null;
		try {
			config = new TabConfig();
			mDb = mDbHelper.getWritableDatabase();

			String sql = "SELECT * FROm TabConfig WHERE companyId=" + companyId;
			Cursor mCur = mDb.rawQuery(sql, null);

			if (mCur.getCount() > 0) {
				mCur.moveToFirst();
				config.setHomepage_title(mCur.getString(mCur
						.getColumnIndex("homepage_title")));
				config.setHomepage_background(mCur.getString(mCur
						.getColumnIndex("homepage_background")));

				config.setLayout1_title(mCur.getString(mCur
						.getColumnIndex("layout1_title")));
				config.setLayout1_background(mCur.getString(mCur
						.getColumnIndex("layout1_background")));

				config.setLayout2_title(mCur.getString(mCur
						.getColumnIndex("layout2_title")));
				config.setLayout2_background(mCur.getString(mCur
						.getColumnIndex("layout2_background")));

				config.setLayout3_title(mCur.getString(mCur
						.getColumnIndex("layout3_title")));
				config.setLayout3_background(mCur.getString(mCur
						.getColumnIndex("layout3_background")));

				config.setVideo_title(mCur.getString(mCur
						.getColumnIndex("video_title")));
				config.setVideo_bg(mCur.getString(mCur
						.getColumnIndex("video_bg")));
				config.setVideo(mCur.getString(mCur.getColumnIndex("video")));

				config.setVideo_thumb(mCur.getString(mCur
						.getColumnIndex("video_thumb")));

				config.setVideo_text(mCur.getString(mCur
						.getColumnIndex("video_text")));

                config.setPromotion_popup(mCur.getString(mCur.getColumnIndex("promotion_popup")));
                config.setPlaceholder(mCur.getInt(mCur.getColumnIndex("placeholder")));

			}
		} catch (Exception ex) {
			Log.d("SaveCompanyDetails", ex.toString());
		}

		return config;
	}

	public boolean SaveVersionNumber(String versionNumber) {
		try {
			System.out.println("versionNumber>>>>>>>>>>>>>>>>>>>"
					+ versionNumber);
			mDb = mDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("VersionNumber", versionNumber);
			System.out.println("ContentValues>>>>>>>>>" + cv);
			mDb.insert("Company", null, cv);
			Log.d("SaveVersionNumber", "informationsaved");
			return true;
		} catch (Exception ex) {
			Log.d("SaveVersionNumber", ex.toString());
			return false;
		}
	}

	public boolean insertTabContent(int cid, int tabId, String heading1,
			String imagePath1, String heading2, String imagePath2,
			String heading3, String imagePath3) {
		try {
			mDb = mDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();

			cv.put("cid", cid);
			cv.put("tabId", tabId);
			cv.put("heading1", heading1);
			cv.put("imagePath1", imagePath1);
			cv.put("heading2", heading2);
			cv.put("imagePath2", imagePath2);
			cv.put("heading3", heading3);
			cv.put("imagePath3", imagePath3);

			mDb.insert("TabContent", null, cv);

			return true;
		} catch (Exception ex) {
			Log.d("SaveTabsDetails", ex.toString());
			return false;
		}
	}

	public boolean updateTabContent(int cid, int tabId, String imagePath1,
			String imagePath2, String imagePath3) {
		try {
			mDb = mDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();

			if (imagePath1 != null && !imagePath1.equals(""))
				cv.put("imagePath1", imagePath1);
			if (imagePath2 != null && !imagePath2.equals(""))
				cv.put("imagePath2", imagePath2);
			if (imagePath3 != null && !imagePath3.equals(""))
				cv.put("imagePath3", imagePath3);

			// mDb.("TabContent", null, cv);
			mDb.update("TabContent", cv, "cid=" + String.valueOf(cid)
					+ " and tabId=" + tabId, null);
			return true;
		} catch (Exception ex) {
			Log.d("SaveTabsDetails", ex.toString());
			return false;
		}
	}

	public Cursor getTabContent(int CId, int tabId) {

		try {
			String sql = "SELECT * FROm TabContent WHERE cid=" + CId
					+ " and tabId=" + tabId;

			Cursor mCur = mDb.rawQuery(sql, null);
			return mCur;

			// return mCur;

		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public boolean SaveTabDetails(String title, String image1, String text1,
			String image2, String text2, String image3, String text3,
			String image4, String text4, String type, String video,
			String heading1, String heading2, String heading3, int CId,
			int tabId) {
		try {
			System.out.println("Values>>>>>>>>>>>>>>>>>>>" + title + " "
					+ image1 + " " + text1 + " " + image2 + " " + text2 + " "
					+ image3 + " " + text3 + " " + image4 + " " + text4 + " "
					+ type + " " + video + " " + heading1 + " " + heading2
					+ " " + heading3);
			mDb = mDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("CId", CId);
			cv.put("Type", type);

			if (tabId == 0) {
				cv.put("Title", title);
				cv.put("Heading1", heading1);
				cv.put("Heading2", heading2);
				cv.put("Heading3", heading3);
			}
			if (tabId == 1) {
				cv.put("Image1", image1);
				cv.put("Text1", title);
			}
			if (tabId == 2) {
				cv.put("Image2", image1);
				cv.put("Text2", title);
			}
			if (tabId == 3) {
				cv.put("Image3", image1);
				cv.put("Text3", title);
			}
			if (tabId == 4) {
				cv.put("Image4", image1);
				cv.put("Text4", title);
				cv.put("Video", video);
			}

			System.out.println("ContentValues>>>>>>>>>" + cv);
			mDb.insert("Tabs", null, cv);
			Log.d("SaveTabsDetails", "informationsaved");
			return true;
		} catch (Exception ex) {
			Log.d("SaveTabsDetails", ex.toString());
			return false;
		}
	}

	public boolean UpdateTabDetails(String title, String image1, String text1,
			String image2, String text2, String image3, String text3,
			String image4, String text4, String type, String video,
			String heading1, String heading2, String heading3, int Id, int CId) {
		try {
			System.out.println("UpdateValues>>>>>>>>>>>>>>>>>>>" + title + " "
					+ image1 + " " + text1 + " " + image2 + " " + text2 + " "
					+ image3 + " " + text3 + " " + image4 + " " + text4 + " "
					+ type + " " + video + " " + heading1 + " " + heading2
					+ " " + heading3);

			mDb = mDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();

			cv.put("Type", type);
			if (Id == 0) {
				cv.put("Title", title);
				cv.put("Heading1", heading1);
				cv.put("Heading2", heading2);
				cv.put("Heading3", heading3);
			}
			// cv.put("Title", title);
			if (Id == 1) {
				cv.put("Image1", image1);
				cv.put("Text1", title);
			}
			if (Id == 2) {
				cv.put("Image2", image1);
				cv.put("Text2", title);
			}
			if (Id == 3) {
				cv.put("Image3", image1);
				cv.put("Text3", title);
			}
			if (Id == 4) {
				cv.put("Image4", image1);
				cv.put("Text4", title);
				cv.put("Video", video);
			}

			System.out.println("ContentValues>>>>>>>>>" + cv);
			// mDb.update("Tabs", cv, "CId=" + String.valueOf(CId) + " and Id="
			// + Id, null);
			mDb.update("Tabs", cv, "CId=" + String.valueOf(CId), null);
			Log.d("UpdateTabsDetails", "informationsaved");
			return true;

		} catch (Exception ex) {
			Log.d("UpdateTabsDetails", ex.toString());
			return false;
		}
	}

	public boolean Updateimages(String imagepath, int CId, int Id, int val) {
		try {
			System.out.println("UpdateTabImageValues>>>>>>>>>>>>>>>>>>>"
					+ imagepath);

			mDb = mDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("Image" + val, imagepath);

			System.out.println("ContentValues>>>>>>>>>" + cv);

			System.out.println("id>>>>>>>>>>>>>" + Id);
			System.out.println("CId>>>>>>>>>>>>>" + CId);
			// mDb.update("Tabs", cv, "CId=" + String.valueOf(CId) + " and Id="
			// + Id, null);
			mDb.update("Tabs", cv, "CId=" + String.valueOf(CId), null);
			Log.d("UpdateTabsDetails", "informationsaved");
			return true;

		} catch (Exception ex) {
			Log.d("UpdateTabsDetails", ex.toString());
			return false;
		}
	}

	public boolean InsertVideos(String videopath, int CId) {
		try {
			System.out.println("UpdateTabImageValues>>>>>>>>>>>>>>>>>>>"
					+ videopath);

			mDb = mDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("CId", String.valueOf(CId));
			cv.put("VideoPath", videopath);

			System.out.println("ContentValues>>>>>>>>>" + cv);

			System.out.println("CId>>>>>>>>>>>>>" + CId);
			mDb.insert("Videos", null, cv);

			Log.d("UpdateTabsDetails", "informationsaved");
			return true;

		} catch (Exception ex) {
			Log.d("UpdateTabsDetails", ex.toString());
			return false;
		}
	}

	public Cursor getVideos(int CId) {
		// TODO Auto-generated method stub

		try {
			String sql = "SELECT VideoPath FROm Videos WHERE CId=" + CId;

			Cursor mCur = mDb.rawQuery(sql, null);
			return mCur;

			// return mCur;

		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public Cursor getUpdatedImages(int CId, int Id) {
		// TODO Auto-generated method stub

		try {
			String sql = "SELECT Image1, Image2, Image3, Image4 FROm Tabs WHERE CId="
					+ CId;// +" and Id="+Id;

			Cursor mCur = mDb.rawQuery(sql, null);
			return mCur;

			// return mCur;

		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public Cursor getBAckData(int currentPosition) {
		// TODO Auto-generated method stub

		try {
			String sql = "SELECT Id, Name, BackgroundImage, Logo FROm Company";

			Cursor mCur = mDb.rawQuery(sql, null);
			// if (mCur.moveToFirst()) {
			// // do {
			// //
			// int size = mCur.getCount();
			// // for (int i = 0; i <= size; i++) {
			// if (currentPosition < size)
			// mCur.moveToPosition(currentPosition);
			// // }
			//
			// // } while (mCur.moveToNext());
			// }
			return mCur;

			// return mCur;

		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public Cursor getTabData(int currentPosition) {
		// TODO Auto-generated method stub

		try {
			String sql = "SELECT Video, Type, Title, Image1, Text1, Image2, Text2, "
					+ "Image3, Text3 FROm Tabs";

			Cursor mCur = mDb.rawQuery(sql, null);
			return mCur;

		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public Cursor getBackgroundImagePath() {
		// TODO Auto-generated method stub

		try {
			String sql = "SELECT BackgroundPath FROm PicturesPath order by id";

			Cursor mCur = mDb.rawQuery(sql, null);
			return mCur;

		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public Cursor getBackgroundImagePathById(int cid) {

		try {
			String sql = "SELECT BackgroundPath FROm PicturesPath where companyId = "
					+ cid + " order by id";

			Cursor mCur = mDb.rawQuery(sql, null);
			return mCur;

		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public Cursor getVersionNumber() {

		try {
			String sql = "SELECT VersionNumber FROm Company WHERE Id=1";
			Cursor mCur = mDb.rawQuery(sql, null);
			return mCur;
		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public int getCompanyVersion(int companyId) {
		int version = -1;
		try {
			String sql = "SELECT VersionNumber FROm Company WHERE companyId="
					+ companyId;
			Cursor mCur = mDb.rawQuery(sql, null);

			if (mCur != null && mCur.getCount() > 0) {
				mCur.moveToFirst();
				version = mCur.getInt(mCur.getColumnIndex("VersionNumber"));
			}
		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}

		return version;
	}

	public boolean SaveBackgroundImagePath(String imagePath, int cid) {
		try {
			System.out.println("SaveImagePath>>>>>>>>>>>>>>>>>>>" + imagePath);

			mDb = mDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("BackgroundPath", imagePath);

			System.out.println("ContentValues>>>>>>>>>" + cv);
			// mDb.update("Tabs", cv, "CId=" + String.valueOf(CId), null);
			int updatedRows = mDb.update("PicturesPath", cv, "companyId="
					+ String.valueOf(cid), null);

			if (updatedRows == 0) {
				cv.put("companyId", cid);
				mDb.insert("PicturesPath", null, cv);
			}
			Log.d("SaveBackgroundImagePath", "informationsaved");
			return true;

		} catch (Exception ex) {
			Log.d("SaveBackgroundImagePath", ex.toString());
			return false;
		}
	}

	public boolean SaveLogoImagePath(String imagePath) {
		try {
			System.out.println("SaveLogoPath>>>>>>>>>>>>>>>>>>>" + imagePath);

			mDb = mDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("LogoPath", imagePath);
			System.out.println("ContentValues>>>>>>>>>" + cv);
			mDb.insert("Logos", null, cv);
			Log.d("SaveLogoImagePath", "informationsaved");
			return true;

		} catch (Exception ex) {
			Log.d("SaveLogoImagePath", ex.toString());
			return false;
		}
	}

	public Cursor getLogoImagePath() {
		// TODO Auto-generated method stub

		try {
			String sql = "SELECT LogoPath FROm Logos";

			Cursor mCur = mDb.rawQuery(sql, null);
			return mCur;

		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public Cursor getTabsDetail(int id) {
		// TODO Auto-generated method stub

		try {
			String sql = "SELECT * FROm Tabs WHERE CId=" + id;

			Cursor mCur = mDb.rawQuery(sql, null);
			return mCur;

		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public void resetAll() {

		try {

			mDb.delete("Company", null, null);
			mDb.delete("Logos", null, null);
			mDb.delete("Videos", null, null);
			mDb.delete("PicturesPath", null, null);
			resetTabs();
		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public void resetTabs() {

		try {
			String sql = "delete from tabs";

			mDb.rawQuery(sql, null);

			mDb.rawQuery("delete from TabContent", null);

		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public Cursor getcompanyName() {

		try {
			String sql = "SELECT Name FROM Company";
			Log.i("my msgs", "in getCompanyName");
			Cursor mCur = mDb.rawQuery(sql, null);
			// if (mCur.moveToFirst()) {
			// // do {
			// //
			// int size = mCur.getCount();
			// // for (int i = 0; i <= size; i++) {
			// if (currentPosition < size)
			// mCur.moveToPosition(currentPosition);
			// // }
			//
			// // } while (mCur.moveToNext());
			// }
			return mCur;

			// return mCur;

		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}

	}

	public boolean isConfigurationPropertyNameExists(String propertyName) {
		mDb = mDbHelper.getReadableDatabase();

		Cursor cursor = mDb.query(true, DataBaseHelper.TABLE_CONFIGURATION,
				null, DataBaseHelper.COLUMN_CONFIGURATION_PROPERTY_NAME
						+ " = ?", new String[] { propertyName }, null, null,
				null, null);

		if (cursor.moveToFirst()) {
			if (cursor.getString(1).equals(propertyName)) {
				return true;
			}
		}

		return false;
	}

	public boolean saveTaxiConfiguration(String propertyName,
			String propertyValue) {

		if (propertyValue != null && !propertyValue.equals("")) {
			if (isConfigurationPropertyNameExists(propertyName)) {
				try {
					mDb = mDbHelper.getWritableDatabase();
					ContentValues cv = new ContentValues();
					cv.put(DataBaseHelper.COLUMN_CONFIGURATION_PROPERTY_VALUE,
							propertyValue);
					mDb.update(DataBaseHelper.TABLE_CONFIGURATION, cv,
							DataBaseHelper.COLUMN_CONFIGURATION_PROPERTY_NAME
									+ " = ?", new String[] { propertyName });
					return true;
				} catch (Exception ex) {
					Log.d(TAG, ex.getMessage());
					return false;
				}
			} else {
				try {
					mDb = mDbHelper.getWritableDatabase();
					ContentValues cv = new ContentValues();
					cv.put(DataBaseHelper.COLUMN_CONFIGURATION_PROPERTY_NAME,
							propertyName);
					cv.put(DataBaseHelper.COLUMN_CONFIGURATION_PROPERTY_VALUE,
							propertyValue);
					mDb.insert(DataBaseHelper.TABLE_CONFIGURATION, null, cv);

					return true;
				} catch (Exception ex) {
					Log.d(TAG, ex.getMessage());
					return false;
				}

			}

		}
		return false;
	}

	public String getConfigurationPropertyValue(String propertyName) {
		
		mDb = mDbHelper.getReadableDatabase();

		Cursor cursor = mDb.query(true, DataBaseHelper.TABLE_CONFIGURATION,
				null, DataBaseHelper.COLUMN_CONFIGURATION_PROPERTY_NAME
						+ " = ?", new String[] { propertyName }, null, null,
				null, null);

		if (cursor.moveToFirst()) {
			return cursor.getString(2);
		}

		return null;
	}

	public AnalyticEvent getAnalyticsEventByDate(String eventAction, String date) {
		AnalyticEvent analyticEvent = null;
		mDb = mDbHelper.getReadableDatabase();

		Cursor cursor = mDb.query(true, DataBaseHelper.TABLE_EVENT, null,
				DataBaseHelper.COLUMN_EVENT_EVENT_ACTION + "=? AND "
						+ DataBaseHelper.COLUMN_EVENT_EVENT_DATE + "=?",
				new String[] { eventAction, date }, null, null, null, null);

		if (cursor.moveToFirst()) {
			analyticEvent = new AnalyticEvent(
					cursor.getString(cursor
							.getColumnIndex(DataBaseHelper.COLUMN_EVENT_EVENT_ACTION)),
					cursor.getString(cursor
							.getColumnIndex(DataBaseHelper.COLUMN_EVENT_EVENT_DATE)),
					cursor.getLong(cursor
							.getColumnIndex(DataBaseHelper.COLUMN_EVENT_EVENT_VALUE)));
		}

		return analyticEvent;
	}

	public boolean saveAnalyticsEvent(String eventAction, long eventValue) {
		long valueFromDatabase = 0;
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = df.format(c.getTime());

		AnalyticEvent analyticEvent = getAnalyticsEventByDate(eventAction,
				formattedDate);
		if (analyticEvent != null) {
			valueFromDatabase = analyticEvent.getEventValue();

			try {
				mDb = mDbHelper.getWritableDatabase();

				ContentValues cv = new ContentValues();
				cv.put(DataBaseHelper.COLUMN_EVENT_EVENT_VALUE,
						valueFromDatabase + eventValue);
				mDb.update(DataBaseHelper.TABLE_EVENT, cv,
						DataBaseHelper.COLUMN_EVENT_EVENT_ACTION + "= ?",
						new String[] { eventAction });
				return true;
			} catch (Exception ex) {
				return false;
			}
		} else {
			try {
				mDb = mDbHelper.getWritableDatabase();

				ContentValues cv = new ContentValues();
				cv.put(DataBaseHelper.COLUMN_EVENT_EVENT_ACTION, eventAction);
				cv.put(DataBaseHelper.COLUMN_EVENT_EVENT_DATE, formattedDate);
				cv.put(DataBaseHelper.COLUMN_EVENT_EVENT_VALUE, eventValue);
				cv.put(DataBaseHelper.COLUMN_EVENT_EVENT_STATE, "New");
				mDb.insert(DataBaseHelper.TABLE_EVENT, null, cv);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}

	public boolean saveAnalyticsEvent(String eventAction, long eventValue,
			HashMap<String, String> extras) {
		long valueFromDatabase = 0;
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = df.format(c.getTime());

		AnalyticEvent analyticEvent = getAnalyticsEventByDate(eventAction,
				formattedDate);
		if (analyticEvent != null) {
			valueFromDatabase = analyticEvent.getEventValue();

			try {
				mDb = mDbHelper.getWritableDatabase();

				ContentValues cv = new ContentValues();
				cv.put(DataBaseHelper.COLUMN_EVENT_EVENT_VALUE,
						valueFromDatabase + eventValue);
				cv.put(DataBaseHelper.COLUMN_EVENT_EXTRAS,
						new Gson().toJson(extras));
				long l = mDb.update(DataBaseHelper.TABLE_EVENT, cv,
						DataBaseHelper.COLUMN_EVENT_EVENT_ACTION + "= ?",
						new String[] { eventAction });
				return true;
			} catch (Exception ex) {
				return false;
			}
		} else {
			try {
				mDb = mDbHelper.getWritableDatabase();

				ContentValues cv = new ContentValues();
				cv.put(DataBaseHelper.COLUMN_EVENT_EVENT_ACTION, eventAction);
				cv.put(DataBaseHelper.COLUMN_EVENT_EVENT_DATE, formattedDate);
				cv.put(DataBaseHelper.COLUMN_EVENT_EVENT_VALUE, eventValue);
				cv.put(DataBaseHelper.COLUMN_EVENT_EVENT_STATE, "New");
				cv.put(DataBaseHelper.COLUMN_EVENT_EXTRAS,
						new Gson().toJson(extras));
				long l = mDb.insert(DataBaseHelper.TABLE_EVENT, null, cv);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}

	public ArrayList<AnalyticEvent> getAllAnalyticsEvents() {
		ArrayList<AnalyticEvent> analyticEventsArray = new ArrayList<AnalyticEvent>();

		try {
			mDb = mDbHelper.getWritableDatabase();
			Cursor cursor = mDb.query(false, DataBaseHelper.TABLE_EVENT, null,
					DataBaseHelper.COLUMN_EVENT_EVENT_STATE + "= ?",
					new String[] { "New" }, null, null, null, null);

			// Cursor cursor = mDb.rawQuery(sql, null);

			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					int eventId = cursor
							.getColumnIndex(DataBaseHelper.COLUMN_EVENT_EVENT_ID);
					

					HashMap<String, String> extras = convertToHashMap(cursor
							.getString(cursor
									.getColumnIndex(DataBaseHelper.COLUMN_EVENT_EXTRAS)));
					AnalyticEvent analyticEvent = new AnalyticEvent(
							cursor.getString(cursor
									.getColumnIndex(DataBaseHelper.COLUMN_EVENT_EVENT_ACTION)),
							cursor.getString(cursor
									.getColumnIndex(DataBaseHelper.COLUMN_EVENT_EVENT_DATE)),
							cursor.getLong(cursor
									.getColumnIndex(DataBaseHelper.COLUMN_EVENT_EVENT_VALUE)),
							extras);

					analyticEventsArray.add(analyticEvent);
				} while (cursor.moveToNext());
			}
		} catch (Exception ex) {
			Log.d("", ex.toString());
		}

		return analyticEventsArray;
	}

	public HashMap<String, String> convertToHashMap(String extra) {
		Gson gson = new Gson();
		return gson.fromJson(extra, HashMap.class);
	}

	public void updateEventState(int eventId, String state) {
		ContentValues cv = new ContentValues();
		cv.put(DataBaseHelper.COLUMN_EVENT_EVENT_STATE, state);
		mDb.update(DataBaseHelper.TABLE_EVENT, cv,
				DataBaseHelper.COLUMN_EVENT_EVENT_ID + " = " + eventId, null);
	}

	public void updateAllEventState(String state) {
		ContentValues cv = new ContentValues();
		cv.put(DataBaseHelper.COLUMN_EVENT_EVENT_STATE, state);
		mDb.update(DataBaseHelper.TABLE_EVENT, cv, null, null);
	}

	public void removeAllEventAnalytics() {
		String sql = "delete From " + DataBaseHelper.TABLE_EVENT;
		mDb.execSQL(sql);
	}

    public boolean setUpdateStatus(Integer id) {
        try {
            mDb = mDbHelper.getWritableDatabase();
            //String sql = "UPDATE TabletInformation SET taxiNo = '" + taxiNo + "'";
            //mDb.rawQuery(sql, null);
            ContentValues cv = new ContentValues();
            cv.put("hasUpdate", id);
            mDb.update("TabletInformation", cv, "", null);

            return true;
        } catch (Exception ex) {
            Log.d("Save Tablet Information", ex.toString());
            return false;
        }
    }
}
