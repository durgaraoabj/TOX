/// <reference path="../../js/typings/jquery.d.ts" />
/// <reference path="../../js/typings/jqueryui.d.ts" />
/// <reference path="../../js/typings/igniteui.d.ts" />
$(function () {
    function contentFunction() {
        var location = $(this)[0] ? $(this)[0] : "Sofia,Bulgaria";
        var imgTemplate = "<img class='map' alt='${value}' src='https://dev.virtualearth.net/REST/V1/Imagery/Map/AerialWithLabels/${value}?mapSize=250,250&format=jpeg&key=${bingKey}'>";
        var data = [{ value: $(this)[0].value, bingKey: mapHelper.bingData() }];
        return $.ig.tmpl(imgTemplate, data);
    }
    $('#IGlogo').igPopover({
        direction: "right",
        position: "start",
        closeOnBlur: false,
        animationDuration: 150,
        maxHeight: null,
        maxWidth: null,
        contentTemplate: $('#contactUs-template').html(),
        headerTemplate: {
            closeButton: true,
            title: "$$(social_title)"
        },
        showOn: "click"
    });
    var popOver = $('#popoverTooltip').igPopover({
        direction: "right",
        position: "start",
        headerTemplate: {
            closeButton: true,
            title: "$$(map_helper_title)"
        },
        closeOnBlur: false,
        animationDuration: 0,
        maxHeight: null,
        maxWidth: 250,
        contentTemplate: contentFunction,
        selectors: "[title]",
        showOn: "focus"
    });
});
//# sourceMappingURL=typescript.js.map