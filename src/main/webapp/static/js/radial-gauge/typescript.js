/// <reference path="../../js/typings/jquery.d.ts" />
/// <reference path="../../js/typings/jqueryui.d.ts" />
/// <reference path="../../js/typings/igniteui.d.ts" />
$(function () {
    $("#radialgauge").igRadialGauge({
        height: "350px",
        width: "100%",
        transitionDuration: 100,
        ranges: [{
                name: "range1",
                brush: "rgba(164, 189, 41, 1)",
                startValue: 70,
                endValue: 100,
                outerStartExtent: 0.55,
                outerEndExtent: 0.65
            }],
        isNeedleDraggingEnabled: true
    });
});
//# sourceMappingURL=typescript.js.map