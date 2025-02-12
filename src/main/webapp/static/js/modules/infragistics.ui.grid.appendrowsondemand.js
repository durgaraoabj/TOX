/*!@license
 * Infragistics.Web.ClientUI Grid AppendRowsOnDemand 19.1.39
 *
 * Copyright (c) 2011-2019 Infragistics Inc.
 *
 * http://www.infragistics.com/
 *
 * Depends on:
 *	jquery-1.9.1.js
 *	jquery.ui.core.js
 *	jquery.ui.widget.js
 *	infragistics.ui.grid.framework.js
 *	infragistics.ui.shared.js
 *	infragistics.util.js
 */
(function(factory){if(typeof define==="function"&&define.amd){define(["./infragistics.ui.grid.framework"],factory)}else{return factory(jQuery)}})(function($){$.widget("ui.igGridAppendRowsOnDemand",$.ui.igWidget,{options:{type:null,chunkSize:25,recordCountKey:null,chunkSizeUrlKey:null,chunkIndexUrlKey:null,defaultChunkIndex:0,currentChunkIndex:0,loadTrigger:"auto",loadMoreDataButtonText:undefined,locale:{loadMoreDataButtonText:undefined}},events:{rowsRequesting:"rowsRequesting",rowsRequested:"rowsRequested"},_loadingIndicator:null,_persistLocalSorting:true,_persistLocalFiltering:true,_recalcLocalSummaries:true,_keepCurrentChunkIndex:true,_callDataRendered:true,_initialProbeForChunkIndex:true,_injectGrid:function(gridInstance){this.grid=gridInstance;this._checkNotSupportedScenarios();this.options.currentChunkIndex=this.options.defaultChunkIndex;if(this.options.type===null){this.options.type=this.grid._inferOpType()}this.grid.dataSource.settings.paging.type=this.options.type||"remote";this._defaultChunkSize=parseInt(this.options.chunkSize,10)*(this.options.defaultChunkIndex+1);this.grid.dataSource.settings.paging.pageSize=this._defaultChunkSize;if(this.options.chunkSizeUrlKey!==null&&this.options.chunkIndexUrlKey){this.grid.dataSource.settings.paging.pageSizeUrlKey=this.options.chunkSizeUrlKey;this.grid.dataSource.settings.paging.pageIndexUrlKey=this.options.chunkIndexUrlKey}if(this.options.recordCountKey!==null){this.grid.dataSource.settings.responseTotalRecCountKey=this.options.recordCountKey}this.grid.dataSource.settings.paging.enabled=true;if(this.options.loadTrigger==="auto"){this._verticalScrollHandler=$.proxy(this._probeForNextChunk,this)}this._appendRecordsHandler=$.proxy(this._appendRecords,this);this._columnSortingHandler=$.proxy(this._columnSorting,this);this._syncCurrentChunkIndexHandler=$.proxy(this._syncCurrentChunkIndex,this);this.grid.element.bind("iggridsortinginternalcolumnsorting",this._columnSortingHandler);this.grid.element.bind("iggriduisoftdirty iggriduidirty",this._syncCurrentChunkIndexHandler)},_dataRendered:function(){var buttonId,container;if(!this._callDataRendered){return}this._keepCurrentChunkIndex=false;this.grid.scrollContainer().css("background-color","white");this.grid.dataSource.settings.paging.pageSize=this.options.chunkSize;this._originalDataSourceCallback=this.grid.dataSource.settings.callback;this._initLoadingIndicator();if(this.options.loadTrigger==="auto"){this.grid.scrollContainer().unbind("scroll",this._verticalScrollHandler);this.grid.scrollContainer().bind("scroll",this._verticalScrollHandler);this.grid.scrollContainer().bind("igscrollscrolling",this._verticalScrollHandler);if(this._initialProbeForChunkIndex||this.options.type==="local"){this._probeForNextChunk()}}this._requestPending=false;this._triggerEvents=true;if(this.options.loadTrigger==="button"){if(!this._buttonRow){buttonId=this.grid.id()+"_loadMoreButton";container=this.grid.options.height?this.grid.scrollContainer():this.grid.container();this._buttonRow=container.append("<div class='ui-iggrid-loadmorebutton'><input type='button' id='"+buttonId+"'></input></div>");this.grid.container().find("#"+buttonId).attr({"data-localeid":"loadMoreDataButtonText","data-localeattr":"value"}).igButton({labelText:this._getLocaleValue("loadMoreDataButtonText"),click:$.proxy(this._nextChunk,this),width:"100%"});if(this.grid.scrollContainer().data("igScroll")!==undefined){this._onIgScrollRendered()}else{this._onIgScrollRenderedHandler=$.proxy(this._onIgScrollRendered,this);this.grid.scrollContainer().on("igscrollrendered",this._onIgScrollRenderedHandler)}}}if(this._loadingIndicator){this._hideLoading()}},_onIgScrollRendered:function(){var loadMoreButton=$(".ui-iggrid-loadmorebutton",this.grid.scrollContainer());if(loadMoreButton.length>0){this.grid.scrollContainer().igScroll("option","syncedElemsV",[loadMoreButton]);this._igScrollUpdateHeightWithButton()}},_igScrollUpdateHeightWithButton:function(){var loadMoreButton=$(".ui-iggrid-loadmorebutton",this.grid.scrollContainer()),scrollHeight=this.grid.element.outerHeight()+loadMoreButton.outerHeight();this.grid.scrollContainer().igScroll("option","scrollHeight",scrollHeight);this.grid.scrollContainer().igScroll("refresh");if(!$.ig.util.isSafari&&$.ig.util.getScrollWidth()===0&&this.grid.scrollContainer().data("igScroll")._vBarContainer){this.grid.scrollContainer().data("igScroll")._vBarContainer.css("z-index",2)}},_checkNotSupportedScenarios:function(){if(this.options.loadTrigger==="auto"&&!this.grid.options.height){throw new Error(this._getLocaleValue("appendRowsOnDemandRequiresHeight"))}if(this.grid.options.virtualization||this.grid.options.rowVirtualization||this.grid.options.columnVirtualization){throw new Error(this._getLocaleValue("virtualizationNotSupported"))}var i,featureName,features=this.grid.options.features,featuresLength=features.length;if(featuresLength===1){return}for(i=0;i<featuresLength;i++){featureName=features[i].name;if(!featureName){continue}featureName=featureName.toLowerCase();switch(featureName){case"groupby":throw new Error(this._getLocaleValue("groupByNotSupported"));case"paging":throw new Error(this._getLocaleValue("pagingNotSupported"));case"cellmerging":throw new Error(this._getLocaleValue("cellMergingNotSupported"))}}},_setOption:function(key,value){this._super(key,value);if(key==="defaultChunkIndex"){throw new Error(this._getLocaleValue("optionChangeNotSupported").replace("{optionName}",key))}if(key==="currentChunkIndex"||key==="chunkSize"){this._keepCurrentChunkIndex=true;this.grid.dataSource.settings.paging.pageSize=(this.options.currentChunkIndex+1)*this.options.chunkSize;this.grid.dataSource.dataBind()}},_registerWidget:$.noop,_unregisterWidget:$.noop,_initLoadingIndicator:function(){this._loadingIndicator=this.grid.container().igLoading().data("igLoading").indicator()},_nextChunk:function(){var noCancel=true;if(this.options.currentChunkIndex>=this.grid.dataSource.pageCount()-1){return}if(this._triggerEvents){noCancel=this._trigger(this.events.rowsRequesting,null,{owner:this,chunkIndex:this.options.currentChunkIndex+1,chunkSize:this.options.chunkSize})}if(noCancel){this._showLoading();this.grid.dataSource.settings.paging.pageSize=this.options.chunkSize;this.grid.dataSource.settings.paging.pageIndex=this.options.currentChunkIndex;this.grid.dataSource.settings.paging.appendPage=true;this._originalDataSourceCallback=this.grid.dataSource.settings.callback;this.grid.dataSource.settings.callback=this._appendRecordsHandler;this._requestPending=true;this.grid.dataSource.nextPage()}},_showLoading:function(){this._loadingIndicator.show()},_hideLoading:function(){this._loadingIndicator.hide()},destroy:function(){var buttonId=this.grid.id()+"_loadMoreButton",container=this.grid.options.height?this.grid.scrollContainer():this.grid.container(),button=container.find("div.ui-iggrid-loadmorebutton");this.grid.container().find("#"+buttonId).igButton("destroy");if(button){button.remove()}this.grid.element.unbind("iggridsortinginternalcolumnsorting",this._columnSortingHandler);this.grid.element.unbind("iggriduisoftdirty iggriduidirty",this._syncCurrentChunkIndexHandler);this.grid.scrollContainer().unbind("scroll",this._verticalScrollHandler);this.grid.scrollContainer().unbind("igscrollscrolling",this._verticalScrollHandler);if(this._onIgScrollRenderedHandler){this.grid.scrollContainer().unbind("igscrollrendered",this._onIgScrollRenderedHandler)}this._superApply(arguments);return this},_appendRecords:function(success,errmsg){var i,currentPage=[],noCancelError,sorting=this.grid.element.data("igGridSorting"),filtering=this.grid.element.data("igGridFiltering"),summaries=this.grid.element.data("igGridSummaries"),selection=this.grid.element.data("igGridSelection");if(success===true){currentPage=this.grid.dataSource.dataView().slice(this.grid.allRows().length-this.grid.pendingTransactions().length)}this._requestPending=false;this.grid.dataSource.settings.paging.pageIndex=0;this.grid.dataSource.settings.paging.appendPage=false;this.grid.dataSource.settings.callback=this._originalDataSourceCallback;for(i=0;i<currentPage.length;i++){if(selection){if(!this.grid.options.primaryKey&&selection.options.persist){selection._createUidForData()}}this.grid.renderNewRow(currentPage[i])}if(sorting){this._keepCurrentChunkIndex=true;if(this.options.type==="remote"&&this.grid.dataSource.settings.sorting.type==="local"){if(!this._persistLocalSorting){this.grid.dataSource.settings.sorting.expressions=[]}else{if(this.grid.dataSource.settings.sorting.expressions.length>0){this._callDataRendered=false;sorting.sortMultiple();this._callDataRendered=true}}}else{sorting._dataRendered()}this._keepCurrentChunkIndex=false}if(filtering){this._keepCurrentChunkIndex=true;if(this.options.type==="remote"&&this.grid.dataSource.settings.filtering.type==="local"){if(!this._persistLocalFiltering){this.grid.dataSource.settings.filtering.expressions=[]}this._callDataRendered=false;filtering.filter(this.grid.dataSource.settings.filtering.expressions,true);this._callDataRendered=true}this._keepCurrentChunkIndex=false}if(summaries){if(this.options.type==="remote"&&this.grid.dataSource.settings.summaries.type==="local"){if(this._recalcLocalSummaries){summaries.calculateSummaries()}}}if(this.grid.scrollContainer().data("igScroll")!==undefined){if(this.options.loadTrigger==="auto"){this.grid.scrollContainer().igScroll("refresh")}else{this._igScrollUpdateHeightWithButton()}}this._hideLoading();if(success===false){noCancelError=this._trigger(this.grid.events.requestError,null,{owner:this,message:errmsg});if(noCancelError){throw new Error(errmsg)}}this.options.currentChunkIndex++;this._keepCurrentChunkIndex=false;if(this._triggerEvents){this._trigger(this.events.rowsRequested,null,{owner:this,chunkIndex:this.options.currentChunkIndex,chunkSize:this.options.chunkSize,rows:currentPage})}this._triggerEvents=true;if(this._initialProbeForChunkIndex||this.options.type==="local"){this._probeForNextChunk()}},_probeForNextChunk:function(){if(this.options.loadTrigger!=="auto"){return}var delta,scrollTop;if(this.grid.scrollContainer().data("igScroll")!==undefined){scrollTop=this.grid.scrollContainer().igScroll("option","scrollTop")}else{scrollTop=this.grid.scrollContainer().scrollTop()}delta=Math.ceil(scrollTop+this.grid.scrollContainer().height())/$(this.grid.element).height();if(delta>=1&&!this._requestPending){this._nextChunk()}else{this._initialProbeForChunkIndex=false}},_columnSorting:function(){this.grid.dataSource.settings.paging.pageSize=this.options.chunkSize*(this.options.currentChunkIndex+1);this._keepCurrentChunkIndex=true},_hidingFinishing:function(){this._keepCurrentChunkIndex=true},_gridCommit:function(){this._keepCurrentChunkIndex=true;this.grid.dataSource._dataView=this.grid.dataSource.data().slice(0,(this.options.currentChunkIndex+1)*this.options.chunkSize)},_internalRowDeleted:function(){var record=this.grid.dataSource.data()[this.grid.dataSource.dataView().length];if(this.options.type==="remote"){return}if(record){this._pushRecordIntoView(record)}this.options.currentChunkIndex=Math.min(this.options.currentChunkIndex,Math.max(Math.ceil(this.grid.dataSource.data().length/this.options.chunkSize)-1,0))},_internalRowAdded:function(row){var updating=this.grid.element.data("igGridUpdating");if(this.grid.options.autoCommit===true){if(this.grid.dataSource.dataView().length>this.options.chunkSize&&this.grid.dataSource.dataView().length%this.options.chunkSize!==0){this.grid.dataSource.dataView().pop();row=updating._combineRowElements(row.row);row.remove()}}},_syncCurrentChunkIndex:function(){if(this.options.type==="remote"&&this.grid.dataSource.settings.filtering.type==="local"){return}if(!this._keepCurrentChunkIndex){if(this.grid.dataSource.settings.paging.pageIndex!==this.options.currentChunkIndex){this.options.currentChunkIndex=this.grid.dataSource.settings.paging.pageIndex}this._keepCurrentChunkIndex=true}},_pushRecordIntoView:function(record){this.grid.renderNewRow(record);this.grid.dataSource.dataView().push(record)},nextChunk:function(){this._triggerEvents=false;this._nextChunk()}});$.extend($.ui.igGridAppendRowsOnDemand,{version:"19.1.39"});return $});(function(factory){if(typeof define==="function"&&define.amd){define("watermark",["jquery"],factory)}else{factory(jQuery)}})(function($){$(document).ready(function(){var wm=$("#__ig_wm__").length>0?$("#__ig_wm__"):$("<div id='__ig_wm__'></div>").appendTo(document.body);wm.css({position:"fixed",bottom:0,right:0,zIndex:1e3}).addClass("ui-igtrialwatermark")})});