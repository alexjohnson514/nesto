package com.example.td_advert.bean;

import com.example.td_advert.constant.TadvertConstants;



public class Company {
	private int companyId;
	private String name;
	private int version;
	private String background_image;
	private String logo;
	private String company_path;
	private TabConfig tabs;
	
	public int getCompanyId() {
		return companyId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + companyId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		if (companyId != other.companyId)
			return false;
		return true;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBackground_image() {
		return background_image;
	}

	public void setBackground_image(String background_image) {
		this.background_image = background_image;
	}

	public String getCompany_path() {
		return company_path;
	}

	public void setCompany_path(String company_path) {
		this.company_path = company_path;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	

	public TabConfig getTabs() {
		return tabs;
	}

	public void setTabs(TabConfig tabs) {
		this.tabs = tabs;
	}

	public int getVideoLoopDuration() {
		if (companyId == 65)
			return TadvertConstants.tutorialVideoDuration;
		return TadvertConstants.timePerVideo;
	}


	public static class TabConfig {
		private String homepage_title;
		private String homepage_background;
		private String layout1_title;
		private String layout1_background;
		private String layout2_title;
		private String layout2_background;
		private String layout3_title;
		private String layout3_background;
		private String video_title;
		private String video_bg;
		private String video;
		private String video_thumb;
		private String video_text;
        private String promotion_popup;
        private Integer placeholder;

        public Integer getPlaceholder() {
            return placeholder;
        }

        public void setPlaceholder(Integer placeholder) {
            this.placeholder = placeholder;
        }

        public String getPromotion_popup() {
            return promotion_popup;
        }

        public void setPromotion_popup(String promotion_popup) {
            this.promotion_popup = promotion_popup;
        }
		
		public String getHomepage_title() {
			return homepage_title;
		}
		public void setHomepage_title(String homepage_title) {
			this.homepage_title = homepage_title;
		}
		public String getHomepage_background() {
			return homepage_background;
		}
		public void setHomepage_background(String homepage_background) {
			this.homepage_background = homepage_background;
		}
		public String getLayout1_title() {
			return layout1_title;
		}
		public void setLayout1_title(String layout1_title) {
			this.layout1_title = layout1_title;
		}
		public String getLayout1_background() {
			return layout1_background;
		}
		public void setLayout1_background(String layout1_background) {
			this.layout1_background = layout1_background;
		}
		public String getLayout2_title() {
			return layout2_title;
		}
		public void setLayout2_title(String layout2_title) {
			this.layout2_title = layout2_title;
		}
		public String getLayout2_background() {
			return layout2_background;
		}
		public void setLayout2_background(String layout2_background) {
			this.layout2_background = layout2_background;
		}
		public String getLayout3_title() {
			return layout3_title;
		}
		public void setLayout3_title(String layout3_title) {
			this.layout3_title = layout3_title;
		}
		public String getLayout3_background() {
			return layout3_background;
		}
		public void setLayout3_background(String layout3_background) {
			this.layout3_background = layout3_background;
		}
		public String getVideo_title() {
			return video_title;
		}
		public void setVideo_title(String video_title) {
			this.video_title = video_title;
		}
		public String getVideo_bg() {
			return video_bg;
		}
		public void setVideo_bg(String video_bg) {
			this.video_bg = video_bg;
		}
		public String getVideo() {
			return video;
		}
		public void setVideo(String video) {
			this.video = video;
		}
		public String getVideo_thumb() {
			return video_thumb;
		}
		public void setVideo_thumb(String video_thumb) {
			this.video_thumb = video_thumb;
		}
		public String getVideo_text() {
			return video_text;
		}
		public void setVideo_text(String video_text) {
			this.video_text = video_text;
		}

		
	}

}
