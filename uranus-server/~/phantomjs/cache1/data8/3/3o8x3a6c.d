   �         )http://weixin.sogou.com/js/pb.nogz.js?v=1     %fX��      % �0�         
     O K           �   	   Server   nginx   Date   Thu, 28 Jan 2016 07:35:32 GMT   Content-Type   application/x-javascript   Last-Modified   Tue, 08 Dec 2015 03:19:00 GMT   Vary   Accept-Encoding   P3P   UCP="CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR"   Expires   Sat, 27 Feb 2016 07:35:32 GMT   Cache-Control   max-age=2592000   Content-Encoding   gzip function sogou_uigs(){
	var paras = uigs_para,
        $d = document,
        UIGS_HEAD = "http://pb.sogou.com/",
        UIGS_CL = "cl.gif",
        UIGS_PV = "pv.gif",
        UIGS_VER = "v2.0",
        START_TIME = getNowTime(),
        uigs_pvlink = getFromPara("uigs_pvpingbackurl", UIGS_HEAD + UIGS_PV),
        uigs_cllink = getFromPara("uigs_clpingbackurl", UIGS_HEAD + UIGS_CL),
        uigs_uuid   = getFromPara("uigs_uuid", getRandom()),
        uigs_cookie = getFromPara("uigs_cookie", "").split(","),
        uigs_pbtag  = getFromPara("uigs_pbtag", "A"),
        uigs_head = null,
        i = null,
        empty_func = function(){},
        oldclick = $d.onclick || empty_func,
        pingbackarray = {},
        pingbackarrayidx = 0,
        //sogou_last_mousedown_time = null,
        //sogou_mousemove_distance = 0,
        //sogou_old_document_mousedown = $d.onmousedown || empty_func,
        //sogou_old_document_mousemove = $d.onmousemove || empty_func,
        //getstop = function(){return (($d.body && $d.body.scrollTop) || ($d.documentElement && $d.documentElement.scrollTop) || 0)},
        docEl = (($d.compatMode && $d.compatMode != "BackCompat") ? $d.documentElement : $d.body);

    if(typeof(uigs_para) == "undefined" || !uigs_para.uigs_productid) {
        window.uigs2PB = function(){}
        return;
    }

    window.uigs2PB = function(uigs_cl, txt, exp_id) {
        var cl_pbstr = ["", "uigs_st=" + parseInt((getNowTime() - START_TIME) / 1000), "uigs_cl=" + uigs_encode(uigs_cl)];
        if (txt) {
            cl_pbstr.push("txt=" + uigs_encode(txt));
        }
        if (exp_id) {
        	cl_pbstr.push("exp_id=" + exp_id);
        }
        uigs_pingback(cl_pbstr.join("&"));
    }


    if(typeof(uigs2_pv) == "undefined") {
        window.uigs2_pv = 1;
        uigs_pv();
    }

    $d.onclick = function(evt){
        var ret = oldclick(evt);
        //paras['mmc'] = (getNowTime() - sogou_last_mousedown_time);
        uigs_click(evt);
        return ret;
    }

    function getNowTime() {
        return (new Date()).getTime();
    }

    function getRandom() {
        return (getNowTime()) * 1000 + Math.round(Math.random() * 1000);
    }

    function getFromPara(pname, defaultV, isNum) {
        if (typeof paras[pname] == "undefined") {
            return defaultV;
        }
        if (isNum){
            return parseInt(paras[pname]);
        }
        return paras[pname];
    }

	function getCookie(name) {
		var dc = $d.cookie, prefix = name + "=", begin = dc.indexOf("; " + prefix), end = null;
		if (begin == -1) {
			begin = dc.indexOf(prefix);
			if (begin != 0)
                return null;
		} else {
			begin += 2;
		}
		end = $d.cookie.indexOf(";", begin);
		if (end == -1) {
			end = dc.length;
		}
		return dc.substring(begin + prefix.length, end);
	}

	function uigs_encode(a) {
		return (typeof(encodeURIComponent) == 'function') ? encodeURIComponent(a): escape(a);
	}

	function getAttr(elem, attr) {
		var ret;
		if (elem) {
			ret = elem[attr];
			if (elem.getAttribute) {
				ret = ret || elem.getAttribute(attr);
			}
		}
		return ret || "";
	}

    function uigs_pingback(pbStr, isPv){
        var pbsrc = [(isPv ? uigs_pvlink : uigs_cllink), build_header(), pbStr || ""].join(""),
            tmp = new Image(),
            idx = pingbackarrayidx;
        pingbackarray[idx] = tmp;
        pingbackarrayidx++;
        tmp.onload = tmp.onerror = tmp.onabort = function(){try{delete pingbackarray[idx]}catch(E){}};
        tmp.src = pbsrc;
    }
	window.uigs_pingback=uigs_pingback;

	function build_header(){
		if (!uigs_head) {
			uigs_head = ["uigs_productid=" + paras.uigs_productid];
			uigs_head.push("uigs_uuid=" + uigs_uuid);
			uigs_head.push("uigs_version=" + UIGS_VER);
			uigs_head.push("uigs_refer=" + uigs_encode($d.referrer || ""));

			var tmp = [], cookieV;
			for (i = 0; i < uigs_cookie.length; i++) {
				if (uigs_cookie[i] && uigs_cookie[i] != "SUV") {
					cookieV = getCookie(uigs_cookie[i]);
					if (cookieV != null){
						tmp.push(uigs_cookie[i] + "=" + cookieV);
					}
				}
			}
			uigs_head.push("uigs_cookie=" + uigs_encode(tmp.join("&")));
			uigs_head = uigs_head.join("&");
		}
		var all_paras = [];
		for (i in paras){
			if (typeof paras[i] != "function" && i != "uigs_productid" && i != "uigs_uuid" && i != "uigs_cookie") {
				all_paras.push(uigs_encode(i) + "=" + uigs_encode(paras[i]));
			}
		}
		all_paras.push("xy=" + docEl.clientWidth + ',' + docEl.clientHeight);

		return ["?", uigs_head, "&", all_paras.join("&"), "&uigs_t=", getRandom()].join("");
	}

	function uigs_pv(){
		if(!getCookie("SUV")) {
			$d.cookie = "SUV=" + getRandom() + ";path=/;expires=Tue, 19-Jan-2046 00:00:00 GMT;domain=sogou.com"
		}
		uigs_pingback("", true);
	}
	
	function uigs_click(evt){
		if ((evt && (evt.button != 0)) || ((!evt) && (window.event.button != 0))) {
			return;  // not left click
		}

		try{
			evt = evt || window.event;
			var srcElem = ((evt.target) ? evt.target : evt.srcElement), tag, uigsflag, dHref, dTxt, tmpTxt, dExpId, tmpExpId;

			while(srcElem && srcElem.tagName) {
				tag = srcElem.tagName.toUpperCase();

				//there is some element you do not want to send click pingback, then add attribute uigs="nouigs"
				uigsflag = getAttr(srcElem, "uigs") || uigsflag || "";

				if(uigsflag == 'nouigs') {
					//do not send
					return;
				}

				dHref = dHref || srcElem.href || "";
				tmpTxt = getAttr(srcElem, "uigs_txt"); //txt can be a's innerHTML or any elemnt's uigs_txt
				tmpExpId = getAttr(srcElem, "uigs_exp_id"); //exp_id is used for interleaving
				if (tag == "A" || tmpTxt || tmpExpId) {
					dTxt = tmpTxt || dTxt || srcElem.innerHTML;
					dExpId = tmpExpId || dExpId || "";
				}

				if (uigsflag && uigsflag != 'id'){ //uigs=="id" means use id
					break;
				}

				if(tag == uigs_pbtag) {
					uigsflag = 'id'; //means use id
				}
				
				if (uigsflag == "id" && getAttr(srcElem, "id")) {
					uigsflag = getAttr(srcElem, "id"); // try to get id from it or its parent node
					break;
				}

				srcElem = srcElem.parentNode;
			}
			
			if (uigsflag && uigsflag != "id"){
				uigs2PB(uigsflag + "&href=" + dHref, (dTxt || "").replace(/<.*?>/g, ""), dExpId);
			}
		} catch(E) {
			alert(E);
		}
	}

	//is scroll to bottom
	/*
	function uigs_judgeBottom(){
	    try{
	        var scrolltop = getstop();
	        if (scrolltop > 100&& (docEl.scrollHeight-docEl.clientHeight-scrolltop)<100){
	            st = "tob="+parseInt((getNowTime()-START_TIME)/1000);    
	            uigs2PB(st);
	            return;
	        }
	    }catch(E){
	    }
	    setTimeout(uigs_judgeBottom, 500);
	}
	setTimeout(uigs_judgeBottom, 1500);
	*/
	
	//mouse monitor function
	/*
	$d.onmousemove = function (evt){
		paras['mml']=(++sogou_mousemove_distance);
		sogou_old_document_mousemove(evt);
	}       
	$d.onmousedown = function (evt){
		sogou_last_mousedown_time = getNowTime();
		sogou_old_document_mousedown(evt);
	}
	*/
}
sogou_uigs();