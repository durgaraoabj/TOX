var _firefox, _focusElem, isMobileOld, scriptsHash = {}, initLoad = false, hasLoaded = false, fadeInterval = 500, _mobile = null,
    lastSelected, initSelected, intervalID, isManual = false, prevControlIconName = null,
    featureTimer, currStyleScripts = [], prevStyleScripts = [], index, isIE, isIE9, isIE8, isIE7, prevTitle = "", prevControlTitle = "",
    windowLoaded = false, hasSampleLoaded = true, windowScrollLeft = 0, windowScrollTop = 0,
    lastUrl, codeViewerPopupVisible, historyChangeFunc, History, a, ie9Bug, startUrl, hamMinWidth,
    // list of samples with full postback
    fullPostbacks = [],
    // check if it is IE9 or lower and reduce max number of history-changes
    limit = isIE9 ? 30 : 1000;

// Reloads the style sheets on the current page
function ReloadStyleSheets() {
	try {
		var linkTags = document.getElementsByTagName("link");
		var len = linkTags.length;
		for (var i = 0; i < len; i++) {
			var link = linkTags[i];
			if (link.rel === "stylesheet") {
				var linkHref = link.href;
				link.href = linkHref && linkHref.match(/[?]$/) ? linkHref.slice(0, -1) : linkHref + "?";
			}
		}
	}
	catch (ex) { }
}

/* BEGIN Application Samples page */
function CreateAppQRCode(qrZoneID, qrImageID, href) {
	var qrOptions = {
		text: href,
		qrZoneID: qrZoneID,
		qrImageID: qrImageID,
		size: '70',
		margin: '0',
		typeNumber: false,
		render: 'image',
		backgroundColor: '#FFFFFF'
	}

	CreateQRCode(qrOptions);
}

function CreateQRCode(qrOptions) {
	if (!qrOptions) return;

	var href = qrOptions.text ? qrOptions.text : "";
	if (href.indexOf("#") >= 0)
		href = href.split('#')[0];
	else
		href = href.split('?')[0];
	qrOptions.text = href;

	var qrZoneID = qrOptions.qrZoneID ? qrOptions.qrZoneID : 'qrZone';
	var qrImageID = qrOptions.qrImageID ? qrOptions.qrImageID : 'qrZoneImage';

	var qrZone = $("#" + qrZoneID);
	if (qrZone && (!qrOptions.hasOwnProperty('isInitLoad') || (!qrOptions.isInitLoad || qrOptions.isInitLoad === true))) {
		var qrType = 'image';

		//// For IE7
		//var a = navigator.userAgent.toLowerCase();
		//index = a.indexOf("msie ");
		//if (index > 0) {
		//    index = parseFloat(a.substr(index + 5));
		//    if (index <= 7)
		//        qrType = 'table';
		//}

		qrZone.html('').qrcode(qrType, qrOptions);
		$(qrZone.find(qrType.replace('image', 'img')).get(0)).attr('id', qrImageID).attr('alt', 'QR Code');
	}
}

/* END Application Samples page */

/* BEGIN Code Viewer Popup */

// show - show/hide codeViewerPopup
// checkValid - fired by permanent timer/interval:
// check if codeViewerPopup should be automatically hidden due to large size of browser
function toggleCodeViewerPopup(show, checkValid) {
	var divs, i, id, copy, obj, input,
		codeDiv, codeDiv$, codeDivHeight, reduceHeight,
		accordion, height, top, left,
		ticks, scrollTimer, div = codeViewerPopupVisible, scroller = document.body;
	if (checkValid) {
		if (!div) {
			return;
		}
		if (div.parentNode && div.offsetWidth) {
			if (div._height === document.documentElement.clientHeight) {
				return;
			}
			show = true;
			scroller = null;
		}
	}
	obj = $("#codeViewerPopup");
	div = obj[0];
	if (!div) {
		return;
	}
	if (show && div._visible && !checkValid) {
		return;
	}
	div._visible = show;
	codeViewerPopupVisible = show ? div : null;
	if (!show) {
		// enable default scrolling
		document.documentElement.style.overflow = document.body.style.overflow = "auto";
	}
	if (!show && checkValid) {
		obj.css("visibility", "hidden");
		return;
	}
	// get around mobile scroll bugs: move codeViewer to body and hide all child elements of body besides codeViewer
	if (!checkValid && isMobilePhone()) {
		$("#sampleBrowserContainer").css("display", show ? "none" : "block");
		$(".footerArea").css("display", show ? "none" : "block");
		if (!div._oldParent) {
			div._oldParent = div.parentNode;
		}
		i = show ? document.body : div._oldParent;
		if (div.parentNode !== i) {
			obj.appendTo(i);
		}
	}
	obj.css("visibility", "visible");
	if (show) {
		if (isMobilePhone()) {
			// to ensure that browser will scroll to top (mobile phones)
			// create invisible INPUT located at the top-left corner and set focus to it
			input = obj.find("input");
			if (!input[0]) {
				input = $("<input id='codeViewerPopupInput' />").css({
					position: "absolute",
					top: "-2px",
					left: "-2px",
					fontSize: "1px",
					height: "1px",
					width: "1px",
					zIndex: -1,
					opacity: 0,
					background: "transparent",
					outline: "none",
					border: "none"
				}).appendTo(div);
			}
			setTimeout(function () {
				try {
					input[0].focus();
				} catch (ignore) { }
			}, 100);
		}
		div._height = height = document.documentElement.clientHeight;
		// find element which controls scroll
		while (scroller) {
			top = scroller.scrollTop;
			left = scroller.scrollLeft;
			if (top || left) {
				break;
			}
			scroller = scroller.parentNode;
		}
		// disable default scrolling
		if (!checkValid) {
			document.documentElement.style.overflow = document.body.style.overflow = "hidden";
			obj.css("opacity", 0).css("height", "auto");
		}
		// ensure that contents of accordion panes are filled: copies from jquery tabs-code-viewer panes
		accordion = $("#codeViewerPopupAccordion");
		divs = accordion.find("DIV");
		i = div._filled ? 0 : divs.length;
		div._filled = true;
		// only once
		while (i-- > 0) {
			codeDiv = divs[i];
			id = codeDiv.id;
			copy = id ? id.indexOf("_copy") : 0;
			if (copy > 0) {
				codeDiv._codeDiv = true;
				codeDiv$ = $(codeDiv);
				codeDiv._oldDisplay = codeDiv$.css("display");
				// temporary hide all accordion views: to calculate height of accordion headers
				codeDiv$.css("display", "none");
				// find html element which contains contents for codeViewer
				copy = $("#" + id.substring(0, copy))[0];
				if (copy) {
					// fill contents for codeViewer
					codeDiv$.html(copy.innerHTML);
				}
			}
		}
		// only once
		if (codeDiv$) {
			// calculate how much height of codeviewer should be reduced
			reduceHeight = accordion[0].offsetHeight;
			// restore original visibility of accordion views
			for (i = 0; i < divs.length; i++) {
				if (divs[i]._codeDiv) {
					divs[i].style.display = divs[i]._oldDisplay;
				}
			}
			// adjust for borders and padding
			reduceHeight += isNaN(i = parseFloat(codeDiv$.css("borderTopWidth"))) ? 0 : i;
			reduceHeight += isNaN(i = parseFloat(codeDiv$.css("borderBottomWidth"))) ? 0 : i;
			reduceHeight += isNaN(i = parseFloat(codeDiv$.css("paddingTop"))) ? 0 : i;
			reduceHeight += isNaN(i = parseFloat(codeDiv$.css("paddingBottom"))) ? 0 : i;
			div._reduceHeight = reduceHeight;
			// stretch accordion
			accordion.css("height", "100%");
		}
		reduceHeight = div._reduceHeight;
		if (reduceHeight) {
			// 15: adjust for larger height of pop-up (10px) and make height of code-viewer slightly less (5px) than calculated height
			codeDivHeight = (height - reduceHeight - 15) / height * 100 + "%";
			for (i = 0; i < divs.length; i++) {
				if (divs[i]._codeDiv) {
					divs[i].style.height = codeDivHeight;
				}
			}
		}
		// attempt to "stretch" height of pop-up to height of browser, because parent of #codeViewerPopup is not body
		obj.css("height", "100%");
		// 15: make height of pop-up slightly larger than calculated height
		obj.css("height", (height + (isMobilePhone() ? 25 : 15)) / (div.offsetHeight || 100) * 100 + "%");
		// animate browser-scrolling to let/top corner with 400ms
		if (left || top) {
			if (isMobilePhone()) {
				scroller.scrollTop = scroller.scrollLeft = 0;
				if (window.scrollTo)
					window.scrollTo(0, 0);
			} else {
				ticks = 10;
				scrollTimer = setInterval(function () {
					if (ticks-- < 1 || !div.parentNode || !div.parentNode.offsetWidth) {
						clearInterval(scrollTimer);
						return;
					}
					scroller.scrollTop = top * ticks / 10;
					scroller.scrollLeft = left * ticks / 10;
				}, 40);
			}
		}
		if (checkValid) {
			return;
		}
	}
	obj.animate({ opacity: show ? 1 : 0 }, 500, null, show ? null : function () {
		obj.css("visibility", "hidden");
	});
}

/* END Code Viewer Popup */


// Detect if the browser is of a mobile phone (excluding tablets)
function isMobilePhone() {
	var mobileDevice = getMobile();
	return mobileDevice !== "none" && mobileDevice.indexOf("tablet") < 0;
}

// Detect if the browser is a mobile one or not (including tablets)
function isMobile() {
	var mobileDevice = getMobile();
	return mobileDevice !== "none";
}

// Get the name of the mobile device if possible
function getMobile() {
	if (!_mobile) {
		var agent = navigator && navigator.userAgent ? navigator.userAgent.toLowerCase() : "";

		if (agent.match(/iphone/i)) {
			_mobile = "IPhone";
		}
		else if (agent.match(/ipad/i)) {
			_mobile = "tablet";
		}
		else if (agent.match(/android/i)) {
			_mobile = "androidtablet";
			if (agent.match(/mobile/i))
				_mobile = "android";
			if (agent.indexOf("android 2.") > 0 || agent.indexOf("android 3.") > 0)
				isMobileOld = true;
		}
		else if (agent.match(/windows phone/i)) {
			_mobile = "windowsphone";
		}
		else if (agent.match(/mobile/i)) {
			_mobile = "unknown";
		}
		else if (agent.match(/tablet pc/i) || agent.match(/tabletpc/i)) {
			_mobile = "none";
		}
		else if (agent.match(/tablet/i)) {
			_mobile = "tablet";
		}
		else {
			_mobile = "none";
		}
	}

	return _mobile;
}

// Set features for mobile devices (tablets and phones)
function SetMobileFeatures() {
	var isMobileDevice = isMobile();
	if (isMobileDevice) {
		var jsFiddle = $("#JSFiddle");
		var downloadButton = $(".downloadButton");
		var hamDownloadButton = $('.hamburgerMenu .navCategoryTitle a[href*="download"]');
		jsFiddle ? jsFiddle.css("display", "none") : "";
		downloadButton ? downloadButton.css("display", "none") : "";
		hamDownloadButton ? hamDownloadButton.css("display", "none") : "";
	}
}

// Sets the width of the sample container to the actual width instead of auto
function SetSampleContainerWidth() {
	var sampleMain = $("#sample");
	var sampleContainer = $("#sampleContainer");
	var tempContainer = $(".tempContainer");
	var body = $('body');
	var containerMinWidth = tempContainer ? tempContainer.css("min-width") : 0;
	var containerMinWidthFloat = parseFloat(containerMinWidth);
	var bodyWidth = body.width();

	if (sampleMain && sampleContainer) {
		var sampleMarginLeft = sampleMain.css("margin-left");
		var sampleMarginRight = sampleMain.css("margin-right");
		var containerNewWidth = bodyWidth >= containerMinWidthFloat ? containerMinWidthFloat : bodyWidth;
		sampleContainer.width(containerNewWidth - parseFloat(sampleMarginLeft) - parseFloat(sampleMarginRight));
	}
}

function loadSampleAjax(url) {
	//hide search results on navigation
	sbSearch.hideSearchResults();
	// scroll to top
	sbCommon.scrollToTop();

	// D.P. 26th Oct 2017 Bug 238481: Use HistoryJS pushState if available, otherwise History.savedStates won't get updated and will fail navigating back to the first page
	var history = History || window.history;
	history.pushState({ "html": url, "pageTitle": null }, "", url);
	var lastUrlClean = GetCleanURL(lastUrl);
	var windowLocClean = GetCleanURL(window.location.href);
				
	if (lastUrlClean && windowLocClean && lastUrlClean != windowLocClean) {
		historyChangeFunc(true);
	}
}

function loadControlSamples(href, li) {
	var $navContainer = $("#menuSamplesContainer"),
		dataPath = href.replace(location.protocol + "//" + location.host, "")
			.replace(MyAppUrlSettings.hostname, "").split("/")[0],
		fromClick = li && li.length,
		li = li || $navContainer.find("a.link-level-2").filter(function () {
			return this.href.indexOf("/" + dataPath + "/") !== -1;
		});

	//$navContainer.find(".js-active").removeClass("js-active");
	//li.addClass("js-active");

	loadSampleAjax(href);

	if (!fromClick) {
		//adjust side accordion after loadSampleAjax (historyFunc) changes selection
		sbSideNav.expandSampleGroup();
	}

}


/**
 * Samples browser common
 */
window.sbCommon = {
	scrollToTop: function () {
		$('html, body').animate({ scrollTop: 0 }, 'fast');
	}
};

/**
 * Side Navigation
 */
window.sbSideNav = {
	initialized: false,
	atBottom: false,
	mobileMinWidth: 960,
	bodyWidth: null,
	css: {
		selectedSample: "js-listItemSelected current",
		controlLinkExpanded: "js-expanded",
		groupExpanded: "js-current",
		listVisible: "show",
		bottom: "bottom",
		menuOpen: "igw-menu-open"
	},
	/* selectors */
	sel: {
		controlGroupLink: ".toggle",
		controlLink: "a.link-level-2",
		controlLinkLI: ".navControlTitle",
		scrollContainer: ".igscroll-container",
	},
	
	/* elements */
	selectedSample: null,
	menuContainer: null,
	navContainer: null,
	/**
	 * Init side menu, pass in $("#controlsMenu"), $("#menuSamplesContainer")
	 * Will call syncSidebarHeight as well.
	 * @param {jQuery} container Main side nav menu container
	 * @param {jQuery} samples Samples ToC list
	 */
	init: function (container, samples) {
		if (this.initialized) {
			return;
		}
		if (container.length && samples.length) {
			this.menuContainer = container;
			this.navContainer = samples;
			this.selectedSample = this.getSampleLink().addClass(this.css.selectedSample);

			this.navContainer.height(this.getAvailableHeight());
			
			this.bodyWidth = $("body").width();
			if (this.bodyWidth < this.mobileMinWidth) {
				//apply igw-menu-open class when the body width is lower than 960px so that the menu can be rendered initially
				this.setMenuOpened();
			}

			if (this.navContainer.igScroll) {
				// TODO: Temp adjustment to hide scroll arrows on IE/Edge (opacity+:before)
				var oldMethod = $.ui.igScroll.prototype._setScrollBarsOpacity;
				$.ui.igScroll.prototype._setScrollBarsOpacity = function (newOpacity) {
					oldMethod.apply(this, arguments);
					if (this._vBarDrag && (this._percentInViewV < 1)) {
						newOpacity = newOpacity < 0 ? 0 : newOpacity;
						this._vBarArrowUp.css("visibility", newOpacity ? "" : "hidden");
						this._vBarArrowDown.css("visibility", newOpacity ? "" : "hidden");
					}
				};
				var oldToSimple = $.ui.igScroll.prototype._toSimpleScrollbar;
				$.ui.igScroll.prototype._toSimpleScrollbar = function () {
					oldToSimple.apply(this, arguments);
					if (this._vBarDrag && (this._percentInViewV < 1)) {
						this._vBarArrowUp.css("visibility", "hidden");
						this._vBarArrowDown.css("visibility", "hidden");
					}
				};
				this.navContainer.igScroll();
				// scroll adjustments
				this.navContainer.find(".igscroll-content").width("100%");
			}
			this.attachEvents();
			this.initialized = true;
			this.syncSidebarHeight();
		}
	},
	attachEvents: function () {
		var self = this;
		$(window).scroll(function (e) {
			self.updatePosition();
		});

		/* Sample click */
		this.navContainer.on("click", "li.navSample a", function (e) {
			var a = $(this);
			// AJAX nav:
			if (!isIE9 && !this.href.startsWith("file:///")) {
				e.preventDefault();
				if (!a.parent().is(self.selectedSample)) {
					self.setSelectedSample(a.parent());
				}
				loadSampleAjax(this.href);
				//close menu
				self.setMenuClosed();
			}
		});

		/* Simple Acordeon menu */
		this.navContainer.on("click", this.sel.controlGroupLink, function (e) {
			e.preventDefault();
			var $this = $(this).parent(),
				$current = self.navContainer.find('.sub-ul.show').prev(".navControlHeader");
				
			if ($current.length && !$current.is($this)) {
				self.openCloseMenuItem($current, ".sub-ul", self.css.groupExpanded);
			}
			self.openCloseMenuItem($this, ".sub-ul", self.css.groupExpanded);
		});

		// Open/Close the navSampleGroups menu of controlTitle (Control expand/collapse)
		this.navContainer.on("click", this.sel.controlLinkLI, function (e) {
			e.preventDefault();
			var lastExpanded = self.navContainer.find('.navSampleGroupsList.show').prev(),
				current = $(this);
			if (lastExpanded.length && !lastExpanded.is(current)) {
				//collapse any opened sample groups
				self.openCloseMenuItem(lastExpanded, ".navSampleGroupsList", self.css.controlLinkExpanded);
			}
			self.openCloseMenuItem($(this), ".navSampleGroupsList", self.css.controlLinkExpanded);
		});

		/** Hamburger */
		//Open the hamburger menu
		$("#hamburger_click").click(function () {
			self.setMenuOpened();
		});

		//close the hamburger menu
		$("#contentLayer").on("click", function () {
			self.setMenuClosed();
		});

	    // Open/Close the navSamples menu of navSampleGroup
		$(".navSampleGroupTitle").click(function () {
		    self.openCloseMenuItem($(this), ".navSamplesList", ".hamburgerMenu");
		});
	},
	refreshScroll: function () {
		this.navContainer.igScroll("refresh");
	},
	/**
	 * Get the sample menu node matching the current URL
	 * @param {string} Optional URL fragmet to look for
	 * @returns {jQuery} The LI node that should be selected
	 */
	getSampleLink: function (startUrl) {
		var navigationLIs, sampleLi = $();
		// find current url to select initial item in list
		if (!startUrl) {
			startUrl = window.MyAppUrlSettings;
			startUrl = startUrl ? startUrl.SampleContentURL : null;
		}
		navigationLIs = this.navContainer.find("li.navSample");
		for (var i = 0; i < navigationLIs.length; i++) {
			if (navigationLIs.eq(i).children("a").attr("href") === startUrl) {
				sampleLi = navigationLIs.eq(i);
				break;
			}
		}
		return sampleLi;
	},
	/**
	 * Updates selected sample with a new target node
	 * @param {jQuery} target The new node to select
	 */
	setSelectedSample: function (target) {
		this.selectedSample.removeClass(this.css.selectedSample);
		this.selectedSample = target.addClass(this.css.selectedSample);
	},
	/**
	 * Open/Close the child menu item of a given menu item
	 * @param {jQuery} item The menu item to toggle
	 * @param {string} childMenuSelector Selector for the child list
	 * @param {string} toggleClass CSS class to toggle on the item
	 */
	openCloseMenuItem: function (item, childMenuSelector, toggleClass) {
		if (item && item.length) {
			var next = item.nextAll(childMenuSelector),
				cssTarget = item.is(this.sel.controlLinkLI) ? item.find(this.sel.controlLink) : item;

			// open if closed
			if (next.css("display") !== "block") {
				next.stop().slideDown($.proxy(sbSideNav.refreshScroll, sbSideNav));
				next.addClass(this.css.listVisible);
				cssTarget.addClass(toggleClass);
			} else {
				// close if open
				next.stop().slideUp($.proxy(sbSideNav.refreshScroll, sbSideNav));
				next.removeClass(this.css.listVisible);
				cssTarget.removeClass(toggleClass);
			}
		}
	},
	/**
	 * Expands the group(s) of the currently selected sample in the hamburger menu
	 * @param {string} urlPath Optional URL path to select link for
	 */
	expandSampleGroup: function (urlPath) {
		var target = this.selectedSample, control, controlGroup, targetControl, targetGroup, self;
		if (!this.initialized || this.selectedSample.length === 0) {
			return;
		}
		if (urlPath) {
			target = this.getSampleLink(urlPath);
		}
		currSelElems = this.selectedSample;
		control = this.navContainer.find('.navSampleGroupsList.show');
		controlGroup = this.navContainer.find('.sub-ul.show');

		if (this.selectedSample.length && !this.selectedSample.is(target)) {
			targetControl = target.closest(".navSampleGroupsList");
			targetGroup = target.closest(".sub-ul");

			//Collapse previous, only close if different
			if (control.length && !control.is(targetControl)) {
				this.openCloseMenuItem(control.prev(this.sel.controlLinkLI), ".navSampleGroupsList", this.css.controlLinkExpanded);
				control = targetControl;
			}
			if (controlGroup.length && !controlGroup.is(targetGroup)) {
				this.openCloseMenuItem(controlGroup.prev(".navControlHeader"), ".sub-ul", this.css.groupExpanded);
				controlGroup = targetGroup;
			}
			this.setSelectedSample(target);
		}
		if (!controlGroup.is(":visible")) {
			this.openCloseMenuItem(this.selectedSample.closest(".navCategoryFeature").children(".navControlHeader"), ".sub-ul", this.css.groupExpanded);
		}
		if (!control.is(":visible")) {
			self = this;
			this.openCloseMenuItem(this.selectedSample.closest(".navControl").children(this.sel.controlLinkLI), ".navSampleGroupsList", this.css.controlLinkExpanded);

			// scroll to selected node after the container is open:
			controlGroup.promise().then(function () {
				self.navContainer.find(self.sel.scrollContainer)
				.stop().animate({
					scrollTop:
						self.selectedSample.offset().top - self.navContainer.offset().top // actual position of node in list
						- self.navContainer.height() / 2 // put it in the ~middle
				}, 250);
			});
		}
	},
	/** Gets the available height for the controls list */
	getAvailableHeight: function () {
		var $window = $(window),
			$footer = $("footer"),
			totalHeight = $window.height(),
			scrollTop = $window.scrollTop(),
			menuTop = this.navContainer.offset().top - scrollTop,
			footerTop = $footer.offset().top;

		if (footerTop <= totalHeight) {
			// footer visible (short page):
			totalHeight -= (totalHeight - footerTop);
		}

		// remove top headers and search space
		totalHeight -= menuTop;
		return totalHeight;
	},
	/**
	 * Sync side nav height to available space (between headers and optionally footer)
	 */
	syncSidebarHeight: function () {
		var $window = $(window), newHeight;
		if (!this.initialized) {
			return;
		}
		if (this.atBottom) {
			this.menuContainer.removeClass(this.css.bottom);
			newHeight = this.getAvailableHeight();
			this.menuContainer.addClass(this.css.bottom);
		} else {
			newHeight = this.getAvailableHeight();
		}

		if (this.navContainer.height() !== newHeight) {
			// set the menu available height
			this.navContainer.height(newHeight);
			this.refreshScroll();
		}
	},
	/** Checks the current page scroll if the side menu should be fixed to the bottom */
	updatePosition: function () {
		var $window = $(window);
		if (!this.initialized || $window.width() <= this.mobileMinWidth) {
			return;
		}
		var $footer = $("footer"),
			totalHeight = $window.height(),
			scrollTop = $window.scrollTop(),
			scrollPos = totalHeight + scrollTop,
			footerTop = $footer.offset().top;

		if (scrollPos > footerTop && footerTop > totalHeight) {
			// at footer position:
			this.atBottom = true;
			this.menuContainer.addClass(this.css.bottom);
			return;
		} else if (this.atBottom) {
			this.atBottom = false;
			this.menuContainer.removeClass(this.css.bottom);
		}

	},
	/**
	 * Returns true if the menu is open on mobile
	 * @returns {boolean}
	 */
	isMenuOpen: function () {
		var body = $('body');
		return body.width() < this.mobileMinWidth && body.hasClass(this.css.menuOpen);
	},
	/** Set menu open class on body so the side menu opens on mobile */
	setMenuOpened: function () {
		var body = $('body');

		body.toggleClass(this.css.menuOpen);
	
		//Ensure top scroll when opening
		sbCommon.scrollToTop();

		// Clip the content on the right if there is not enough space
		//body.css("overflow-x", "hidden");
	},
	/** Remove menu open class on body so the side menu closes on mobile */
	setMenuClosed: function () {
		var body = $('body');
		body.removeClass(this.css.menuOpen);

		// Remove scroll prevention
		//body.css("overflow-x", "auto");
	}
}

/**
 * Search
 */
window.sbSearch = {
	initialized: false,
	$mainContainer: null,
	$searchContainer: null,
	$searchLoading: null,
	$searchResults: null,
	$searchField: null,
	$searchBtn: null,
	$clearBtn: null,
	$hint: null,

	$noResultsMsg: null,
	noResultsTimer: null,

	init: function () {
		this.$searchField = $("#searchField");
		if (this.initialized || !this.$searchField.length) {
			return;
		}
		this.$mainContainer = $("#mainContainer");
		this.$searchContainer = $("#searchContainer");
		this.$searchLoading = this.$searchContainer.children(".loaderCss"),
		this.$searchResults = this.$searchContainer.children(".search-results");
		this.$searchBtn = $("#searchButton");
		this.$clearBtn = $(".searchFieldContainer .searchClearIcon");
		this.$hint = $(".searchFieldContainer .searchHint");
		this.$noResultsMsg = $(".igw-no-search-result");

		this.attachHandlers();
		if (this.$searchField.val()) {
			try {
				searchSamples();
			} catch (ignore) { }
		} else {
			this.$searchField[0]._lastTxt = "";
		}
		this.initialized = true;
	},
	attachHandlers: function () {
		var field = this.$searchField;
		this.$searchContainer.on("click", ".pagination a", function (e) {

			// handle search paging
			var href = $(e.target).data("href");
			if (!href) {
				return;
			}
			sbSearch.getSearchResults(href);
			return false;
		})
		.on("click", ".search-element a", function (e) {

			// handle sample nav - relative part only
			loadControlSamples(MyAppUrlSettings.hostname +
				/^(?:https?:\/\/[^\/]+)?\/(.*)$/.exec(this.href).pop())
			return false;
		})
		.on("click", ".previous-btn", function () {
			sbSearch.clearSearhcField();
			sbSearch.hideSearchResults();
		});
		field.bind({
			blur: function () {
				if (!this.value) {
					sbSearch.$hint.removeClass("displayNone");
					sbSearch.hideSearchResults();
				} else if (!sbSearch.$searchContainer.is(":visible")) {
					sbSearch.clearSearhcField();
				}
				if (this._delay) {
					clearTimeout(this._delay);
					this._delay = null;
				}
				this._lastTxt = this.value;
			}, keydown: function (e) {
				var key = e.keyCode || e.which;
				//input._delay = setTimeout(function () {
				//	searchSamples();
				//}, 600);
				if (this.value.length || String.fromCharCode(key)) {
					sbSearch.$hint.addClass("displayNone");
					sbSearch.$clearBtn.addClass("searchClearIconValue");
				}
				if (key === 13) {
					sbSearch.searchSamples();
				}
			}, "paste, drop, cut": function () {
				setTimeout(function () {
					sbSearch.searchSamples();
				}, 10);
			}
		});

		this.$searchBtn.click(function () {
			if (sbSearch.$searchField.val()) {
				sbSearch.searchSamples();
			} else {
				field.focus();
			}
		}).mousedown(function (e) {
			e.preventDefault();
		});

		this.$hint.click(function () {
			field.focus();
		}).mousedown(function (e) {
			e.preventDefault();
		});
	},
	searchSamples: function (searchTag) {
		var field = this.$searchField,
			input = field[0],
			txt = field.val().trim();
		if (searchTag === undefined || searchTag === null) {
			searchTag = input._all;
		}
		if (input._all !== searchTag) {
			input._all = searchTag;
		} else if (txt === input._lastTxt) {
			return;
		}
		input._lastTxt = txt;

		if (!txt) {
			// no empty search (engine treats it as "all")
			this.hideSearchResults();
			return;
		}

		this.$hint.addClass("displayNone");
		this.$clearBtn.addClass("searchClearIconValue");

		this.getSearchResults(MyAppUrlSettings.hostname + "search?query=" + txt);
	},
	getSearchResults: function (url) {
		this.$searchLoading.css("display", "block");
		// use sample loader on initial:
		if (this.$mainContainer.css("display") !== "none") {
			$('#dvLoading').css("display", "block");
		}
		$.get(url)
		.done(function (data, statusText, xhr) {
			if (statusText === "success") {
				sbSearch.$searchResults.html(data);
				sbSearch.showSearchResults();

				if (sbSideNav.isMenuOpen()) {
					//close menu
					sbSideNav.setMenuClosed();
					sbSearch.$searchField[0].blur();
				}
			} else if (statusText == "nocontent") {
				sbSearch.hideSearchResults();
				sbSearch.showNoResults();
			}
		})
		.fail(function () {
			console.log("error");
		})
		.always(function () {
			sbSearch.$searchLoading.css("display", "none");
			$('#dvLoading').css("display", "none");
		})
	},
	hideSearchResults: function () {
		if (!this.initialized) {
			return;
		}
		if (!this.$searchContainer.is(":visible")) {
			return;
		}
		//clear field:
		this.clearSearhcField();

		// show samples
		this.$mainContainer.show({ effect: "fade", easing: "easeInOutCubic" });
		this.$searchContainer.hide({ effect: "fade", easing: "easeInOutCubic" });
	},

	clearSearhcField: function () {
		if (this.$searchField.val()) {
			this.$searchField.val("");
			this.$searchField[0]._lastTxt = "";
			this.$hint.removeClass("displayNone");
			this.$clearBtn.removeClass("searchClearIconValue");
		}
	},
	showSearchResults: function () {
		sbCommon.scrollToTop();
		this.$searchContainer.show({ effect: "fade", easing: "easeInOutCubic" });
		this.$mainContainer.hide({ effect: "fade", easing: "easeInOutCubic" });
	},
	showNoResults: function () {
		if (this.noResultsTimer) {
			clearTimeout(this.noResultsTimer);
		}
		this.$noResultsMsg.stop().fadeIn(function () {
			sbSearch.noResultsTimer = setTimeout(function () {
				sbSearch.$noResultsMsg.fadeOut();
			}, 3000);
		});
	}
}

$(function () {
	$('.headerIcon_click').click(function () {
		var i, index = "index.html", href = window.location.href;
		if (href.indexOf("file:") >= 0) {
			i = href.lastIndexOf("/");
			if (i >= 0) {
				if (href.indexOf(index) > 0) {
					return;
				}
				var origHref = href;
				href = href.substring(0, i);
				if (!(origHref.indexOf("application-samples.html") > 0 || origHref.indexOf("getting-started.html") > 0 || origHref.indexOf("getting-started-mobile.html") > 0)) {
					i = href.lastIndexOf("/");
					if (i >= 0) {
						href = href.substring(0, i);
					}
				}
				window.location = href + "/" + index;
				return;
			}
		}
		window.location = MyAppUrlSettings ? MyAppUrlSettings.hostname : window.location.origin;
	});

	$(window).resize(function () {
		SetMobileFeatures();
		sbSideNav.updatePosition();
		sbSideNav.syncSidebarHeight();
	});

	// initialize search
	sbSearch.init();

	// TODO: These two should be [re]moved, also defined in sb-sample.js:
	initLoad = true;
	lastUrl = window.location.href;

	// TODO: Is all of this still needed?
	try {
		a = navigator.userAgent.toLowerCase();
		if (a.indexOf("firefox") > 0)
			_firefox = true;
		index = a.indexOf("msie ");
		if (index > 0) {
			isIE = true;
			index = parseFloat(a.substr(index + 5));
			if (index >= 10) {
				index = parseFloat(document.documentMode);
			}
			isIE9 = index < 10;
			isIE8 = index < 9;
			isIE7 = index <= 7;
		}
	}
	catch (exc) { }

	SetMobileFeatures();

	// Intialize Code Viewer
	prettyPrint();
	$(".jq-ui-tabs").tabs({

	});
	initSelectBehavior();

	// Intialize side menu
	sbSideNav.init($("#controlsMenu"), $("#menuSamplesContainer"));
	// Expand to current sample
	sbSideNav.expandSampleGroup();

	// TODO: relevant still?
	// To fix issue with Mobile IE 10 and viewport width detection
	if (navigator.userAgent.match(/IEMobile\/10\.0/)) {
		var msViewportStyle = document.createElement("style");
		msViewportStyle.appendChild(
            document.createTextNode(
                "@-ms-viewport{width:auto!important;zoom:1;user-zoom:fixed;}"
            )
        );
		document.getElementsByTagName("head")[0].
            appendChild(msViewportStyle);
	}

	// add hover effects to non-tab navigation (similar to tab)
	//$("li.no-tab").hover(function () {
	//	$(this).addClass("ui-state-hover");
	//}, function () {
	//	$(this).removeClass("ui-state-hover");
	//});

	//$(".dd-more").click(function() {
	//	var path = $(this).parent().attr("data-path");
	//	$(".more-menu[data-path='" + path + "-more']").toggleClass("show");
	//	$(".more-arrow").toggleClass( "flip" );
	//});
	//$(document).mouseup(function (e) {
	//	var container = $("ul.js-visible:first li.dd-more");

	//	if (!container.is(e.target) // if the target of the click isn't the container...
	//		&& container.has(e.target).length === 0) // ... nor a descendant of the container
	//	{
	//		$("ul.more-menu.show").removeClass("show");
	//		$(".more-arrow.flip").removeClass("flip");
	//	}
	//});
});


/* BEGIN Code Viewer */

// that method is called on initialization and on async history change by UpdateContent()
function initSelectBehavior() {
	if (isMobile()) {
		$(".codeViewerSelectAll").css("display", "none");
	} else {
		var codeBlocks = $("pre"), i = codeBlocks.length;
		while (i-- > 0) {
			if ($(codeBlocks[i].parentNode).hasClass("code-viewer")) {
				codeBlocks = codeBlocks.not(codeBlocks[i]);
			}
		}
		// do not use click in order to allow custom drag-select
		codeBlocks.mousedown(function (e) {
			this._x = e ? (e.clientX || e.pageX) : 0;
		}).mouseup(function (e) {
			var x = e ? (e.clientX || e.pageX) : 0;
			if (!x || !this._x || Math.abs(x - this._x) < 5) {
				selectContent(this);
			}
		});
	}
	// ensure that buttons have listeners
	// Note: global methods like
	//   $("body").on("click", ".codeViewerCollapsed", function (){...});
	// may fail in some mobile devices like Windows Phone.
	// Therefore, use explicit javascript handlers with validations if they were already attached.
	var elem = $(".codeViewerCollapsed");
	if (elem[0] && !elem[0].onclick) {
		elem[0].onclick = function () {
			toggleCodeViewerPopup(true);
		};
	}
	elem = $(".codeViewerPopupHideButton");
	if (elem[0] && !elem[0].onclick) {
		elem[0].onclick = function () {
			toggleCodeViewerPopup();
		};
	}
	// ensure that accordion was created
	elem = $("#codeViewerPopupAccordion");
	if (elem[0] && !elem.data("accordion")) {
		elem.accordion();
	}
}
function selectAllClick(elem) {
	var div, divs = $(elem.parentNode).find(".code-viewer"), i = divs.length;
	while (i-- > 0) {
		div = divs[i];
		if (div.offsetWidth > 10 && div.offsetHeight > 10) {
			break;
		}
	}
	var cv = div ? $(div).find("pre")[0] : null;
	if (cv && cv.offsetHeight) {
		selectContent(cv);
	}
}
function selectContent(elem) {
	try {
		var range;
		if (document.createRange) {
			range = document.createRange(), selection = document.getSelection();
			range.selectNodeContents(elem);
			selection.removeAllRanges();
			selection.addRange(range);
		}
		else if (document.body.createTextRange) {
			range = document.body.createTextRange();
			range.moveToElementText(elem);
			range.select();

		}
	} catch (ignore) { }
	try {
		document.execCommand("copy");
	} catch (ignore) { }
}
/* END Code Viewer */

