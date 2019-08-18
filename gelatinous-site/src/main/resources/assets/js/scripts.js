"use strict";

function render_all_timestamps() {
    document.querySelectorAll(".post-date").forEach(function(el) {
        var date = new Date(el.getAttribute("datetime"));
        el.textContent = date.toLocaleDateString("en-GB", {
            day: "numeric",
            month: "long",
            year: "numeric"
        });
    });
}
document.addEventListener("DOMContentLoaded", render_all_timestamps);

document.querySelectorAll(".dialog-dismiss").forEach(el => {
    el.addEventListener("click", evt => {
        evt.preventDefault();
        el.parentNode.removeAttribute("open");
    });
});

function clean(node) {
    for (var n = 0; n < node.childNodes.length; n ++) {
        var child = node.childNodes[n];
        if (child.nodeType === 8 ||
            (child.nodeType === 3 && !/\S/.test(child.nodeValue))) {
            node.removeChild(child);
            n --;
        }
        else if(child.nodeType === 1) {
            clean(child);
        }
    }
}
document.addEventListener("DOMContentLoaded", evt => clean(document));
