package com.hy.superemsg.db;

public interface DBColumns {
	String ID = "_id";
	String TIME = "_time";
	String CATEGORY_NAME = "_categoryName";
	String CATEGORY_ID = "_categoryId";

	public interface SmsColumns {
		String SMS_CONTENT = "_smsContent";
		
	}
	public interface HolidayColumns {
		String HOLIDAY_CONTENT = "_holidayContent";
	}
	public interface MmsColumns {
		String MMS_NAME = "_mmsName";
		String MMS_CONTENT = "_mmsContent";
		String MMS_PIC_URL = "_mmsPicUrl";
	}

	public interface RingColumns {
		String RING_NAME = "_ringName";
		String RING_SINGER = "_ringSinger";
		String RING_PRICE = "_ringPrize";
		String RING_USE_COUNT = "_ringUseCount";
	}

	public interface GameColumns {
		String GAME_NAME = "_gameName";
		String GAME_ICON_URL = "_gameIconUrl";
		String GAME_FILE_URL = "_gameFileUrl";
		String GAME_VERSION = "_gameVersion";
		String GAME_PUBLISHER = "_gamePublisher";
		String GAME_STYLE = "_gameStyle";
		String GAME_INTRODUCE = "_gameIntroduce";
		String GAME_FILE_SIZE = "_gameFileSize";
		String GAME_DOWNLOAD = "_gameDownload";
		String GAME_SCREEN_SHOT_LIST = "_gameScreenShotList";
	}

	public interface NewsColumns {
		String NEWS_TITLE = "_newsTitle";
		String NEWS_LEADS = "_newsLeads";
		String NEWS_FOCUS_PIC_URL = "_newsFocusPicUrl";
		String NEWS_PIC_URL = "_newsPicUrl";
		String NEWS_CONTENT = "_newsContent";
	}

	public interface CartoonColumns {
		String CARTOON_NAME = "_cartoonName";
		String CARTOON_AUTHOR = "_cartoonAuthor";
		String CARTOON_INTRODUCE = "_cartoonIntroduce";
		String CARTOON_COVER_PIC_URL = "_cartoonCoverPicUrl";
	}

	public interface CartoonDramaColumns {
		String CARTOON_ID = "_cartoonId";
		String DRAMA_NAME = "_dramaName";
		String DRAMA_FILE_URL = "_dramaFileUrl";
		String DRAMA_PIC_COUNT = "_dramaPicCount";
	}
}
