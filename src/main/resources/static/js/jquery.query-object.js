!new function(e){var t=e.separator||"&",n=e.spaces===!1?!1:!0,r=(e.suffix===!1?"":"[]",e.prefix===!1?!1:!0),i=r?e.hash===!0?"#":"?":"",u=e.numbers===!1?!1:!0;jQuery.query=new function(){var e=function(e,t){return void 0!=e&&null!==e&&(t?e.constructor==t:!0)},r=function(e){for(var t,n=/\[([^[]*)\]/g,r=/^([^[]+)(\[.*\])?$/.exec(e),i=r[1],u=[];t=n.exec(r[2]);)u.push(t[1]);return[i,u]},s=function(t,n,r){var i=n.shift();if("object"!=typeof t&&(t=null),""===i)if(t||(t=[]),e(t,Array))t.push(0==n.length?r:s(null,n.slice(0),r));else if(e(t,Object)){for(var u=0;null!=t[u++];);t[--u]=0==n.length?r:s(t[u],n.slice(0),r)}else t=[],t.push(0==n.length?r:s(null,n.slice(0),r));else if(i&&i.match(/^\s*[0-9]+\s*$/)){var o=parseInt(i,10);t||(t=[]),t[o]=0==n.length?r:s(t[o],n.slice(0),r)}else{if(!i)return r;var o=i.replace(/^\s*|\s*$/g,"");if(t||(t={}),e(t,Array)){for(var c={},u=0;u<t.length;++u)c[u]=t[u];t=c}t[o]=0==n.length?r:s(t[o],n.slice(0),r)}return t},o=function(e){var t=this;return t.keys={},e.queryObject?jQuery.each(e.get(),function(e,n){t.SET(e,n)}):t.parseNew.apply(t,arguments),t};return o.prototype={queryObject:!0,parseNew:function(){var e=this;return e.keys={},jQuery.each(arguments,function(){var t=""+this;t=t.replace(/^[?#]/,""),t=t.replace(/[;&]$/,""),n&&(t=t.replace(/[+]/g," ")),jQuery.each(t.split(/[&;]/),function(){var t=decodeURIComponent(this.split("=")[0]||""),n=decodeURIComponent(this.split("=")[1]||"");t&&(u&&(/^[+-]?[0-9]+\.[0-9]*$/.test(n)?n=parseFloat(n):/^[+-]?[1-9][0-9]*$/.test(n)&&(n=parseInt(n,10))),n=n||0===n?n:!0,e.SET(t,n))})}),e},has:function(t,n){var r=this.get(t);return e(r,n)},GET:function(t){if(!e(t))return this.keys;for(var n=r(t),i=n[0],u=n[1],s=this.keys[i];null!=s&&0!=u.length;)s=s[u.shift()];return"number"==typeof s?s:s||""},get:function(t){var n=this.GET(t);return e(n,Object)?jQuery.extend(!0,{},n):e(n,Array)?n.slice(0):n},SET:function(t,n){var i=e(n)?n:null,u=r(t),o=u[0],c=u[1],a=this.keys[o];return this.keys[o]=s(a,c.slice(0),i),this},set:function(e,t){return this.copy().SET(e,t)},REMOVE:function(t,n){if(n){var r=this.GET(t);if(e(r,Array)){for(tval in r)r[tval]=r[tval].toString();var i=$.inArray(n,r);if(!(i>=0))return;t=r.splice(i,1),t=t[i]}else if(n!=r)return}return this.SET(t,null).COMPACT()},remove:function(e,t){return this.copy().REMOVE(e,t)},EMPTY:function(){var e=this;return jQuery.each(e.keys,function(t){delete e.keys[t]}),e},load:function(e){var t=e.replace(/^.*?[#](.+?)(?:\?.+)?$/,"$1"),n=e.replace(/^.*?[?](.+?)(?:#.+)?$/,"$1");return new o(e.length==n.length?"":n,e.length==t.length?"":t)},empty:function(){return this.copy().EMPTY()},copy:function(){return new o(this)},COMPACT:function(){function t(n){function r(t,n,r){e(t,Array)?t.push(r):t[n]=r}var i="object"==typeof n?e(n,Array)?[]:{}:n;return"object"==typeof n&&jQuery.each(n,function(n,u){return e(u)?void r(i,n,t(u)):!0}),i}return this.keys=t(this.keys),this},compact:function(){return this.copy().COMPACT()},toString:function(){var r=[],u=[],s=function(e){return e+="",n&&(e=e.replace(/ /g,"+")),encodeURIComponent(e)},o=function(t,n,r){if(e(r)&&r!==!1){var i=[s(n)];r!==!0&&(i.push("="),i.push(s(r))),t.push(i.join(""))}},c=function(e,t){var n=function(e){return t&&""!=t?[t,"[",e,"]"].join(""):[e].join("")};jQuery.each(e,function(e,t){"object"==typeof t?c(t,n(e)):o(u,n(e),t)})};return c(this.keys),u.length>0&&r.push(i),r.push(u.join(t)),r.join("")}},new o(location.search,location.hash)}}(jQuery.query||{});
