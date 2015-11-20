    var title;
    var body;
    var source;
    var myBody;
    var myTitle;
    var textSize;
	var contentWidth;

    function getTitle(){
        myTitle = document.getElementById("title");
        if (window.news) {
            title = window.news.getTitle();
            myTitle.innerHTML = title;
        }
    }

    function getSource(){
        var mySource = document.getElementById('source');
        if (window.news) {
            source = window.news.getSource();
            mySource.innerHTML = source;
        }
    }

    function getBody(){
        myBody = document.getElementById('articlebody');
        if (window.news) {
            body = window.news.getBody();
            myBody.innerHTML = body;
        }
    }

    function getAdvertisements() {
        var ad = document.getElementById("ad");
        if (window.news) {
            var text = window.news.getAdvertisements();
            ad.innerHTML = text;
        }
    }

	function getVideos() {
		var myVideos = document.getElementById("video");
		if (window.news) {
            var text = window.news.getVideos();
            myVideos.innerHTML = text;
        }
	}

	function getTopAd() {
		var myTopAd = document.getElementById("topad");
		if (window.news) {
            var text = window.news.getTopAd();
            myTopAd.innerHTML = text;
        }
	}

	function getContentWidth() {
		contentWidth = parseInt(document.body.clientWidth) - 20;
		if(window.screen.availWidth < 800) {
			contentWidth = 280;
		}
		return contentWidth;
	}

	function getImgHeight(width, height) {
		return parseInt(height * contentWidth / width);
	}

	function getWidth() {
		return contentWidth;
	}

    function getTextSize() {
        if (window.news) {
            textSize = window.news.getTextSize();

            switch(textSize) {
            case 0:
                showSuperBigSize();
                break;
            case 1:
                showBigSize();
                break;
            case 2:
                showMidSize();
                break;
            case 3:
                showSmallSize();
                break;
            }
        }
    }

    // 相关新闻
    function getRelation() {
        var relation = document.getElementById('relation');

        if (window.news) {
            var content = window.news.getRelation();
            relation.innerHTML = content;
        }
    }

	// 热点相关新闻
    function getHotNews() {
        var hots = document.getElementById('hotnews');

        if (window.news) {
            var content = window.news.getHotsRelation();
            hots.innerHTML = content;
        }
    }

    // 顶部相关新闻
    function getTopRelation() {
    	var topRelation = document.getElementById('topRelation');
    	
    	if (window.news) {
    		var content = window.news.getTopRelation();
    		topRelation.innerHTML = content;
    	}
    }

	function getSharesLay() {
		var sharelo = document.getElementById('artshare');
    	
    	if (window.news) {
    		var content = window.news.getSharesLay();
    		sharelo.innerHTML = content;
    	}
	}

	function startShare(i){
		if (window.news) {
            window.news.startShare(i);
        }
	}

    function playVideo(i) {
        if (window.news) {
            window.news.playVideo(i);
        }
    }

    function showImg(i) {
        if (window.news) {
            window.news.showImg(i);
        }
    }

	//网页中图片加载完成后替换默认图片
	function replaceDefaultImg(i, j, width, height) {
		document.getElementById(j).style.display = 'none';
		document.getElementById(i).width = width;
		document.getElementById(i).height = height;
		document.getElementById(i).style.marginBottom = '-16px'
		document.getElementById(i).style.display = 'block';
		
	}

	//视频图片加载完成后替换默认图片
	function replaceDefaultImgForVideo(imgId, defaultImgId, width, height) {
		document.getElementById(defaultImgId).style.display = 'none';
		document.getElementById(imgId).width = getWidth();
		document.getElementById(imgId).height = getImgHeight(width, height);
		document.getElementById(imgId).style.marginBottom = '-16px'
		document.getElementById(imgId).style.display = 'block';
	}

	//设置视频控件中播放按钮位置
	function positionPlayBtn(playBtnId, width, height) {
		document.getElementById(playBtnId).style.visibility = 'visible'
		document.getElementById(playBtnId).style.position = 'absolute';
		document.getElementById(playBtnId).style.width = 80;
		document.getElementById(playBtnId).style.height = 80;
		document.getElementById(playBtnId).style.left = (getWidth() - 80) / 2;
		document.getElementById(playBtnId).style.top = (getImgHeight(width, height) - 80) / 2;
	}

	function resizeWidght(id, width, height) {
		document.getElementById(id).width = width;
		document.getElementById(id).height = height;
		}

    // 跳转相关新闻
    function startRelation(i) {
        if (window.news) {
            window.news.startRelation(i);
        }
    }

	// 跳转热点相关新闻
    function startHotsRelation(i) {
        if (window.news) {
            window.news.startHotsRelation(i);
        }
    }

    // 跳转顶部相关新闻
    function startTopRelation(i) {
    	if (window.news) {
    		window.news.startTopRelation(i);
    	}
    }

    // 跳转评论
    function startHotComments(i) {
        if (window.news) {
            window.news.startHotComments(i);
        }
    }

    // 跳转推广
    function startAdvertisements(i, j) {
        if (window.news) {
            window.news.startAdvertisements(i, j);
        }
    }

    function showSuperBigSize() {
        myBody.style.fontSize = "24px";
        myBody.style.lineHeight = "140%";
    }

    function showBigSize() {
        myBody.style.fontSize = "20px";
        myBody.style.lineHeight = "140%";
    }

    function showMidSize() {
        myBody.style.fontSize = "18px";
        myBody.style.lineHeight = "140%";
    }

    function showSmallSize() {
        myBody.style.fontSize = "14px";
        myBody.style.lineHeight = "140%";
    }

    function initialize(){
		getContentWidth();
		getTopAd();
		getVideos();
		getTitle();
        getSource();
        getBody();
        getTextSize();
        getRelation();
		getHotNews();
        getTopRelation();
		getSharesLay();
        getAdvertisements();
    }

    window.onload = function(){
        initialize();
    }

