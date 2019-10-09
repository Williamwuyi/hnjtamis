/**
 * This file includes the required ext-all js and css files based upon "theme" and "direction"
 * url parameters.  It first searches for these parameters on the page url, and if they
 * are not found there, it looks for them on the script tag src query string.
 * For example, to include the neptune flavor of ext from an index page in a subdirectory
 * of extjs/examples/:
 * <script type="text/javascript" src="../../examples/shared/include-ext.js?theme=neptune"></script>
 * 用于调试加载ext-all.js的判断
 */
(function() {
    function getQueryParam(name) {
        var regex = RegExp('[?&]' + name + '=([^&]*)');

        var match = regex.exec(location.search) || regex.exec(path);
        return match && decodeURIComponent(match[1]);
    }

    function hasOption(opt, queryString) {
        var s = queryString || location.search;
        var re = new RegExp('(?:^|[&?])' + opt + '(?:[=]([^&]*))?(?:$|[&])', 'i');
        var m = re.exec(s);

        return m ? (m[1] === undefined || m[1] === '' ? true : m[1]) : false;
    }

    function getCookieValue(name){
        var cookies = document.cookie.split('; '),
            i = cookies.length,
            cookie, value;

        while(i--) {
           cookie = cookies[i].split('=');
           if (cookie[0] === name) {
               value = cookie[1];
           }
        }

        return value;
    }

    var scriptEls = document.getElementsByTagName('script'),
    path = scriptEls[scriptEls.length - 1].src,
    rtl = getQueryParam('rtl'),
    theme = getQueryParam('theme') || 'gray',//缺省主题
    includeCSS = !hasOption('nocss', path),
    neptune = (theme === 'neptune'),
    repoDevMode = getCookieValue('ExtRepoDevMode'),
    suffix = [],
    i = 1,
    neptunePath;
  
    rtl = rtl && rtl.toString() === 'true'
    while (i--) {
        path = path.substring(0, path.lastIndexOf('/'));
    }
        
    if (theme && theme !== 'classic') {
        suffix.push(theme);
    }
    if (rtl) {
        suffix.push('rtl');
    } 

    suffix = (suffix.length) ? ('-' + suffix.join('-')) : '';
    
    if (includeCSS) {
        document.write('<link rel="stylesheet" type="text/css" href="resources/css/ext-all' + suffix + '.css"/>');
        document.write('<link rel="stylesheet" type="text/css" href="resources/css/desktop.css"/>');
    }
    document.write('<script type="text/javascript" src="'+path+'/ext/ext-all' + (rtl ? '-rtl' : '') + '.js"></script>');
    document.write('<script type="text/javascript" src="'+path+'/ext/ext-lang-zh_CN.js"></script>');
    document.write('<script type="text/javascript" src="'+path+'/core/ChineseSort.js"></script>');
    document.write('<script type="text/javascript" src="'+path+'/core/DateTime.js"></script>');
    document.write('<script type="text/javascript" src="'+path+'/core/Accessory.js"></script>');
    document.write('<script type="text/javascript" src="'+path+'/core/EditList.js"></script>');
    document.write('<script type="text/javascript" src="'+path+'/core/Select.js"></script>');
    document.write('<script type="text/javascript" src="'+path+'/core/SelectTree.js"></script>');
    document.write('<script type="text/javascript" src="'+path+'/core/SelectImage.js"></script>');
    document.write('<script type="text/javascript" src="'+path+'/core/SelectObject.js"></script>');
    document.write('<script type="text/javascript" src="'+path+'/core/MultiSelect.js"></script>');
    if (neptune) {
        // since document.write('<script>') does not block execution in IE, we need to 
        // makes sure we prevent ext-theme-neptune.js from executing before ext-all.js
        // normally this can be done using the defer attribute on the script tag, however
        // this method does not work in IE when in repoDevMode.  It seems the reason for
        // this is because in repoDevMode ext-all.js is simply a script that loads other
        // scripts and so Ext is still undefined when the neptune overrides are executed.
        // To work around this we use the _beforereadyhandler hook to load the neptune
        // overrides dynamically after Ext has been defined.
        /*neptunePath = //(repoDevMode ? path + '/..' : path) +
            'resourecs/ext-theme-neptune/build/ext-theme-neptune' +
            (repoDevMode ? '-dev' : '') + '.js';
        
        if (repoDevMode &&  window.ActiveXObject) {
            Ext = {
                _beforereadyhandler: function() {
                    Ext.Loader.loadScript({ url: neptunePath });
                }
            };
        } else {
            document.write('<script type="text/javascript" src="' + neptunePath + '" defer></script>');
        }*/
    }
    document.write('<script type="text/javascript" src="'+path+'/eap-ext.js"></script>');
})();