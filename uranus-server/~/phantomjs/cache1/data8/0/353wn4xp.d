   �         (http://www.sogou.com/sug/css/m3.v.20.css     %�X��      % ��@         
     O K           �   	   Server   nginx   Date   Thu, 28 Jan 2016 07:35:33 GMT   Content-Type   text/css   Last-Modified   Tue, 24 Nov 2015 02:52:24 GMT   Vary   Accept-Encoding   P3P  CP="CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR", CP="CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR", CP="CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR"   Expires   Tue, 26 Jul 2016 07:35:33 GMT   Cache-Control   max-age=15552000   Content-Encoding   gzip @charset "gbk";
.mt5{margin-top:20px;}
.pt10{padding-top:5px;}
.suggestion {
    width:648px;
    /*_width: 645px;*/
    position:absolute;
    border:1px solid #B5B5B5;
    background-color:#EFEFEF;
    top:53px;
    left:0px;
    font-family:arial, sans-serif;
    font-size:12px;
    z-index:2;
}
.suggestion *{zoom:1;}

@media screen and (-webkit-min-device-pixel-ratio:0) {
    .suggestion  { top:52px;  }
}
.green{color:#008000;}
.suggestion a {
    outline:none;
}
.suglist {
    width:233px;
    list-style:none;
    font-size:14px;
    line-height:27px;
    padding:4px 0;
    float:left;
}
.suglist li {
    padding:0 9px;
    cursor:pointer;
    zoom:1;
    position:relative;
}
.suglist li span{display:inline-block;width:8px;height:11px;background:url(../images/sugarr2.gif) no-repeat 0 0px;_background:url(../images/sugarr2.gif) no-repeat 0 3px;margin:0 0 0 15px;_margin:5px 0 0 15px; }
.suglist li span.sugg-loading{width:14px;height:14px;background:url(../images/loading_14x14.gif) no-repeat 0 0;_background:url(../images/loading_14x14.gif) no-repeat 0 3px;}
/*.suglist li.over span{background:url(../images/sugarr3.gif) no-repeat;_background:url(../images/sugarr3.gif) no-repeat 0 3px;}*/

.suglist li em.n0{width:15px;height:15px;margin:0 9px 0 0;display:inline-block; vertical-align:middle; position:relative;}
@media screen and (-webkit-min-device-pixel-ratio:0) {
    .suglist li em.n0 { top:-2px; }
}

.suglist li.over, .slhover {
    background:#F3F3F3;
}
.suginner {
    /*border:1px solid #E0E0E0;*/
    /*background:#F9F9F9 url(../images/sugbg2.v.1.0.gif) repeat-y;*/
    background: #fff;
    zoom:1;
}
.nobg .suginner{background:#fff;}
.nobg .suglist{width:648px;_width: 645px;}
.suginner_nobg {
    background:none;
}
.suginner:after {
    clear:both;
    content:" ";
    visibility:hidden;
    overflow:hidden;
    height:0;
    display:block;
}
.sugc * { margin: 0; }
.sugc {
    float:left;
    width:393px;
    /*margin-left:10px;*/
    _display:inline;
    height:272px;
    padding-top:6px;
    /*border:1px solid #fff;*/
    border-left:1px solid #f0f0f0;
}
.sugc a {
    text-decoration:none;
}
.sugc a:hover{
    text-decoration:underline;
}
.sugtop {
    display:block;
    padding-bottom:3px;
    margin-bottom:6px;
    zoom:1;
}
.sugtop:hover {
    background-color:#ECEEF2;
}
.sugtype {
    line-height:25px;
    font-size:14px;
    padding:2px 0 4px;
    display:block;
}
.sugtype img{_margin:5px 0;}
.sugctitle {
    font-size:12px;
    font-weight:normal;
    color:#646464;
    line-height:21px;
    padding:0 6px;
    background:url(../images/sugctitle.gif) no-repeat;
    clear:both;
    zoom:1;
}
.sugctitle a {
    color:#646464;
}
.querylist {
    line-height:19px;
    background:url(../images/querylist.gif) no-repeat;
    height:210px;
}
.qitem {
    display:block;
    zoom:1;
    cursor:pointer;
    padding-bottom:5px;
    background:url(../images/qitem.gif) no-repeat;
    background-image:expression(this.previousSibling==null?'none':'');
    text-decoration:none !important;
}
.qitem:first-child {
    border-top:none;
    background-image:none;
}
.qitem:hover {
    background-color:#ECEEF2;
}
.qtitle {
    font-size:13px;
    display:block;
    font-weight:normal;
    padding-top:11px;
    height:21px;
}
.qsummary, .qcite {
    padding:0 7px;
    display:block;
}
.qsummary {
    color:#434343;
}
.qcite {
    color:#008000;
}
.morerst {
    /*font-family:simsun, serif;*/
    line-height:25px;
}
.morerst a {
    color:#666;
}
.moreinfo {
    border-collapse:collapse;
    line-height:21px;
    margin:3px 0 0 10px;
}
.moreinfo td {
    padding:0;
    white-space:nowrap;
}
.sugft, .sugfb {
    line-height:20px;
    padding:0 6px;
}
.sugft {
    margin-top:-3px;
    color:#646464;
    display:block;
}
.sugfb {
    color:#008000;
    margin-bottom:6px;
}
.swbox {
    border-collapse:collapse;
    margin:0 0 4px 6px;
    width:340px;
}
.swbox th, .swbox td {
    vertical-align:top;
}
.swbox td {
    height:22px;
}
.swbox th {
    font-weight:normal;
    color:#666;
    text-align:left;
    width:5em;
}
.softbtn_l, .softbtn_r {
    display:inline-block;
    background:url(../images/softbtn.png) no-repeat;
    color:#fff;
    height:24px;
    line-height:22px;
    font-size:13px;
    text-decoration:none !important;
}
.softbtn_l {
    padding-left:11px;
}
.softbtn_r {
    padding-right:11px;
    background-position:right top;
    cursor:pointer;
}
.minfo {
    margin-bottom:20px;
    zoom:1;
}
.minfolist {
    padding:0 6px;
    list-style:none;
    margin-top:-5px;
}
.minfolist li {
    height:21px;
}
.mlabel {
    color:#666;
    font-weight:normal;
}
.mscore {
    font-size:13px;
}
.trailer {
    font-weight:bold;
    padding-left:19px;
    background:url(../images/trailer.gif) no-repeat left;
    margin-left:6px;
    display:block;
    line-height:25px;
    width:240px;
}
.mpic {
    float:right;
    padding:1px;
    background-color:#fff;
    border:1px solid #ccc;
    margin:8px 8px 20px 0;
}
.mrcmd {
    margin:8px 6px;
}
.mrcmd a {
    color:#426BBD;
}
.sugtype a, .weblist dt a, .moreinfo a, .morerst a, .se_embed_title a {
    display:block;
    padding:0 6px;
    zoom:1;
    text-decoration:none !important;
}
.siteico {
    background:url(../images/dl.gif) no-repeat 6px;
    padding-left:26px !important;
    display:inline-block;
}
.minfolist a, .mrcmd a {
    padding-right:1em;
}
.sugtype a:hover, .weblist dt a:hover, .moreinfo a:hover, .mrcmd a:hover, .morerst a:hover, .minfolist a:hover, .trailer:hover, .se_qlist a:hover, .se_epnumlist li a:hover, .se_embed_title a:hover, .se_eplist a:hover,  .se_videolist a:hover {
    background-color:#e5eaf3 !important;
    text-decoration:none !important;
}
.s_a a:hover{
    background-color:#e5eaf3 !important;
    text-decoration:underline;
}
.moreinfo a:hover {
    text-decoration:underline !important;
}
.sohuvbtn {
    width:97px;
    height:24px;
    line-height:24px;
    background:url(../images/sohuvbtn.gif) no-repeat;
    color:#fff;
    text-decoration:none !important;
    padding-right:6px;
    display:block;
    text-align:right;
    margin-left:6px;
}
.loading {
    background:url(../images/loading.gif) no-repeat center 84px;
    text-align:center;
    color:#999;
    font-size:14px;
    padding-top:140px;
}
.se_showpic1 {
    float:left;
    padding:1px;
    border:1px solid #C4C4C4;
    background-color:#fff;
    margin:0 10px 5px 0;
    position:relative;
}
.se_showpic2 img{
    padding:1px;
    border:1px solid #C4C4C4;
    background-color:#fff;
}
.se_showpic1 img, .se_videopic img {
    display:block;
}
.se_infotab {
    border-collapse:collapse;
    line-height:1.6;
    /*	margin-bottom:10px;
    */	*display:inline;
}
.se_infotab th {
    font-weight:normal;
    text-align:left;
    white-space:nowrap;
    color:#666;
}
.se_infotab th, .se_infotab td {
    padding:0;
    vertical-align:top;
}
.se_nextpage, .se_prevpage {
    height:15px;
    display:inline-block;
    background:url(../images/nextpage.gif) no-repeat;
    overflow:hidden;
    width:15px;
    text-decoration:none!important;
}
.se_prevpage {
    background:url(../images/prevpage.gif) no-repeat;
}
.se_nextpage {
    margin-left:-1px;
}
.se_endpage {
    filter:alpha(opacity=50);
    opacity: .5;
}
.se_tvpagebar {
    clear:both;
    width:331px;
    height:26px;
    padding:5px 0 3px;
}
.se_setpages {
    float:right;
    margin-top:3px;
    display:inline;
}
.se_epnumlistbox {
    width:260px;
    height:26px;
    overflow:hidden;
}
.se_epnumlist {
    height:24px;
    line-height:20px;
    margin:0;
    padding:0;
    list-style:none;
    width:9999px;
}
.se_epnumlist li {
    width:47px;
    height:24px;
    text-align:center;
    float:left;
    margin-right:4px;
}
.se_epnumlist li a {
    display:block;
    zoom:1;
}
.se_epnumlist li.cur {
    background:url(../images/curep.gif) no-repeat;
    font-weight:bold;
}
.se_epnumlist li.cur a {
    color:#757575;
    text-decoration:none !important;
}
.se_epdetail {
    margin:0 0 8px;
    padding:0;
    list-style:none;
    float:left;
    border-left:1px solid #E4E4E4;
    border-top:1px solid #E4E4E4;
    max-width:331px;
    /*_width:expression((this.clientWidth >331) ? "331px" : "auto" );*/
}
.se_epdetail li {
    width:32px;
    text-align:center;
    border-bottom:1px solid #E4E4E4;
    border-right:1px solid #E4E4E4;
    float:left;
}
.se_epdetail a {
    display:block;
    border-top:1px solid #fff;
    border-left:1px solid #fff;
    width:31px;
    height:28px;
    line-height:28px;
    filter:progid:DXImageTransform.Microsoft.gradient(startcolorstr=#F9F9F9, endcolorstr=#F1F1F1, gradientType=0);
    background:-moz-linear-gradient(top, #F9F9F9, #F1F1F1);
    background:-webkit-gradient(linear, 0 0, 0 bottom, from(#F9F9F9), to(#F1F1F1));
    background:-o-linear-gradient(top, #F9F9F9, #F1F1F1);
}
.se_epdetail li a:hover, .se_epdetail li.cur a {
    border-color:#E2E2E2;
    text-decoration:none;
    background:#E2E2E2;
    filter:none;
}
.newep {
    position:absolute;
    top:-5px;
    right:-5px;
}
.ep7 {
    border:none;
    width:auto;
    *width:100%;
    margin-top:5px;
    clear:both;
}
.ep7 li {
    position:relative;
    margin-right:4px;
    border:1px solid #E4E4E4;
}
.se_vsitebtn {
    padding:0 9px 0 24px;
    height:19px;
    line-height:18px;
    *line-height:19px;
    display:inline-block;
    text-decoration:none !important;
    color:#666 !important;
    position:relative;
    border:1px solid #CACACA;
    border-radius:2px;
    filter:progid:DXImageTransform.Microsoft.gradient(startcolorstr=#ffffff,endcolorstr=#F4F4F4,gradientType=0);
    background:-moz-linear-gradient(top, #fff, #F4F4F4);
    background:-webkit-gradient(linear, 0 0, 0 bottom, from(#fff), to(#F4F4F4));
    background:-o-linear-gradient(top, #fff, #F4F4F4);
    /*background:url(../images/vsitebtn.gif) no-repeat;*/
}
.se_vsiteico {
    border:none;
    position:absolute;
    left:5px;
    top:2px;
}
.se_hintvideo {
    margin-bottom:6px;
    clear:both;
    color:#666;
    zoom:1;
}
.playnow {
    display:block;
    width:100px;
    margin-top:3px;
}
.playnow img {
    display:block;
}
.se_playico {
    width:50px;
    height:50px;
    position:absolute;
    left:50%;
    top:50%;
    margin:-25px 0 0 -25px;
    visibility:hidden;
    cursor:pointer;
}
a:hover .se_playico {
    background:url(../images/playhover.png) no-repeat;
    _background-image:none;
    _filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=crop, src=images/playhover.png);
    visibility:visible;
}
a:active .se_playico {
    background:url(../images/playactive.png) no-repeat;
    _background-image:none;
    _filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=crop, src=images/playactive.png);
    visibility:visible;
}
.se_vtimebg, .se_vtime {
    top:68px;
    position:absolute;
    text-align:left;
}
.se_vtimebg, .se_vtimebg2, .se_vtimebg3 {
    height:17px;
    background-color:#000;
    filter:alpha(opacity=50);
    opacity:.5;
    cursor:pointer;
}
.se_vtimebg {
    width:120px;
    left:1px;
}
.se_vtimebg2, .se_vtime2 {
    position:absolute;
    bottom:1px;
    left:1px;
}
.se_vtime, .se_vtime2 {
    line-height:17px;
    color:#fff;
    left:5px;
    cursor:pointer;
}
.se_vtimebg3, .se_vtime3 {
    position:absolute;
    right:1px;
    bottom:1px;
    width:40px;
    cursor:pointer;
}
.se_vtime3 {
    line-height:17px;
    color:#fff;
    text-align:center;
}
.morehint {
    text-align:right;
    color:#999;
}
.morehint a {
    color:#999 !important;
    background:url(../images/morehint.gif) no-repeat left;
    padding-left:19px;
    font-family:simsun;
    display:inline-block;
    text-decoration:none !important;
}
.se_eplist {
    clear:both;
    zoom:1;
}
.se_eplist a {
    display:inline-block;
    padding:3px 4px;
}
.se_starbar {
    padding-left:6px;
}
.se_starbar strong {
    color:#FF4700;
    font:16px Arial, Helvetica, sans-serif;
}
.se_livebtn {
    width:100px;
    height:30px;
    line-height:30px;
    margin:9px 0 0 6px;
    color:#fff!important;
    text-decoration:none !important;
    display:inline-block;
    text-indent:28px;
    background:url(../images/playnow.gif) no-repeat;
}
.se_wikiitem {
    color:#666;
    font-weight:normal;
    padding-left:6px;
}
.se_wiki_intro {
    line-height:1.8;
    overflow:hidden;
    zoom:1;
    margin-left:-4px;
    float:left;
    display:inline;
}
.se_wiki_intro p {
    margin:0 0 6px;
    padding:0;
}
.se_qlist, .se_videolist2, .se_tvlist, .se_piclist2 {
    list-style:none;
    margin:0;
    padding:0;
    line-height:1.7;
}
.se_videopic {
    padding:1px;
    border:1px solid #C4C4C4;
    background-color:#fff;
    display:block;
    margin-bottom:2px;
    position:relative;
    text-decoration:none !important;
}
.se_videolist2 .se_playico {
    top:50%;
}
.se_videolist2 li, .se_tvlist li {
    width:87px;
    float:left;
    _display:inline;
    line-height:normal;
    text-align:center;
    margin-left:15px;
    margin-left:expression(this.previousSibling==null?'0':'15px');
}
.se_videolist2 li:first-child, .se_tvlist li:first-child, .se_piclist2 li:first-child {
    margin-left:0;
}
.se_tvlist li {
    width:105px;
    position:relative;
    margin-left:15px;
    margin-left:expression(this.previousSibling==null?'0':'15px');
}
.se_tvlist li .se_vtimebg, .se_tvlist li .se_vtime {
    width:90px;
    top:49px;
}
.se_videolist {
    list-style:none;
    margin:0 0 12px;
    padding:0;
    zoom:1;
    line-height:1.5;
}
.se_videolist li {
    float:left;
    position:relative;
    margin-left:26px;
    _display:inline;
    _margin-left:expression(this.previousSibling==null?'0':'26px');
    width:124px;

}
.se_videolist li:first-child {
    margin-left:0;
}
.se_vsource {
    top:69px;
    *top:70px;
    right:7px;
    position:absolute;
    line-height:17px;
    color:#fff;
}
.se_qlist {
    clear:both;
    padding-top:6px;
    line-height:1.7;
}
.se_gtext {
    color:#999;
}
.se_embed_resultitem:after, .se_embed_videoitem:after, .se_embed_piclist:after, .se_videolist:after {
    clear:both;
    content:" ";
    visibility:hidden;
    overflow:hidden;
    height:0;
    display:block;
}
.se_newvideo {
    background:url(../images/newvideo.png) no-repeat;
    position:absolute;
    right:0;
    top:0;
    width:18px;
    height:22px;
    _background-image:none;
    _filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=crop, src=images/newvideo.png);
}
.se_embed_title {
    zoom:1;
    font-size:14px;
}
.se_videolist, .se_videolist2 {
    padding-left:6px;
}
.se_qlist a {
    display:inline-block;
    padding:0 6px;
}
.se_eplist a:hover, .se_videolist a:hover, .se_epnumlist li a:hover {
    text-decoration:underline !important;
}
.tvtitle {
    display:block;
    padding:3px 0;
}
.tvtitle:hover {
    background-color:#e5eaf3;
    text-decoration:underline;
}
.suginner a em {
    text-decoration:none;
}
.se_newlist {
    line-height:25px;
    margin:5px 0 0;
    padding:0 6px;
    list-style:none;
    color:#666;
}
.se_more {
    font-family:simsun, serif;
    float:right;
}
.se_subtitle {
    color:#666;
    font-weight:normal;
    font-size:12px;
}
a.s_button1{display:block;width:105px;height:24px;line-height:24px;background:url(../images/s_botton1.jpg);color:#fff; text-align:center;margin:6px 0 0 0;}
#header a.s_button1{display:block;width:80px;height:24px;line-height:24px;background:url(../images/s_botton1.jpg);color:#fff;padding:0 3px 0 21px; text-align:center;margin:6px 0 0 0;}
.su_table{}
.su_table th{background:#F7F0EB;height:21px;color:#646464;font-weight:normal;text-align:left;padding:0 0 0 6px;}
.su_table td{background:url(../images/tdbgline.gif) repeat-x bottom ;height:33px;padding:0 0 0 6px;color:#666;}
.su_table td img{ vertical-align:middle;}
.su_table td.su_more{background:none;}
.su_table td.su_more a{color:#426BBD; text-decoration:underline;}
.su_more a{color:#426BBD; text-decoration:underline;}
.su_title{font-size:12px;font-weight:bold;margin:0 10px 0 0;color:#000;}
.xzmain{}
.xzmain ul{padding:10px 0 0 5px;padding:5px 0 0 5px;}
.xzmain ul li{float:left;width:33%;height:56px;list-style:none;}
.xzmain ul li a{ cursor:pointer;}
.xzmain ul li img{float:left;display:inline;margin:-3px 7px 0 0;*margin:-5px 7px 0 0; position:relative;}
.xzmain ul li a:hover{ text-decoration:none;color:#000;}
.hui0{color:#000;}
.hui9{color:#999;}
.hui6{color:#666;}
.bold{font-weight:bold;}
.lineh{line-height:1.7;}
.lineh2{line-height:2;}
.lineh3{line-height:1.8;}

.su_table2{}
.su_table2 th{background:#EEF2E8;height:21px;color:#646464;font-weight:normal;padding:0 0 0 6px;}
.su_table2 td{background:url(../images/tdbgline.gif) repeat-x bottom ;height:33px;padding:0 0 0 6px;color:#666;}
.su_table2 td img{ vertical-align:middle;}
.su_table2 td.su_more{background:none;}
.su_table2 td.su_more a{color:#426BBD; text-decoration:underline;}
.su_title2{font-size:12px;font-weight:bold;margin:0 10px 0 0;color:#000;}
.su_table2 td.no{background:none;}
.se_infotab2 td.su_more{background:none;}
.se_infotab2 td.su_more a{color:#426BBD; text-decoration:underline;}
.scd1{display:inline-block;background:url(../images/d1.jpg) no-repeat;width:18px;height:18px;}
.scd1:hover{background:url(../images/d1-.jpg) no-repeat;}
.scd2{display:inline-block;background:url(../images/d3.jpg) no-repeat;width:18px;height:18px;}
.scd2:hover{background:url(../images/d3-.jpg) no-repeat;}
.scd3{display:inline-block;background:url(../images/d4.jpg) no-repeat;width:18px;height:18px;}
.scd3:hover{background:url(../images/d4-.jpg) no-repeat;}
.s_a a.scd11 {
    background: url("../images/d1.jpg") no-repeat scroll right 3px #f9f9f9;
    display: inline-block;
    line-height: 24px;
    padding: 0 20px 0 0px;
    background-color:#f9f9f9 !important;
    text-decoration:none;
}
.s_a a.scd11:hover{background: url("../images/d1-.jpg") no-repeat scroll right 3px transparent;}
.scd12{    background: url("../images/d4.jpg") no-repeat scroll transparent;
    display: inline-block;
    line-height: 24px;
    float:left;
    margin:0px 10px 0 0;
    width:18px;
    height:18px;
    cursor:pointer;
}
.scd12:hover{background: url("../images/d4-.jpg");cursor:pointer;}
.button1{width:84px;height:21px;line-height:21px;background:url(../images/buttonbg1.jpg) no-repeat;padding:0 0 0 10px; text-align:center;color:#666;display:block;}
.p_a .button2{width:72px;height:24px;line-height:24px;background:url(../images/button1.jpg) no-repeat;padding:0 0 0 10px; text-align:center;color:#fff;display:block;margin:5px 0 0 0;}
.p_a a{margin:0 10px 0 0;}
#header a.button2{color:#fff;}
.button3{width:111px;height:24px;line-height:24px;background:url(../images/img10.jpg) no-repeat;text-align:center;color:#fff;display:inline-block;margin:0px 10px 10px 0;}
.button3_no{margin-right:0;}
.button5{height:24px;line-height:24px;background:url(../images/img10_.gif) no-repeat left top;text-align:center;color:#fff !important;display:inline-block;margin:0px 10px 10px 0;padding:0 0px 0 10px;}
.button5 span{background:url(../images/img10_.gif) no-repeat; background-position:right -24px;display:inline-block;padding:0 10px 0 0;}
.button5_no{margin-right:0;}
.button4{width:66px;height:24px;line-height:24px;background:url(../images/img12.jpg) no-repeat;text-align:center;color:#fff;display:inline-block;margin:5px 10px 10px 0;}
.s_a .button1:hover,.s_a .p_a .button2:hover,.s_a .button3:hover,.s_a .button4:hover,a.s_button1:hover{ text-decoration:none;}
.weather2 {
    border-collapse: collapse;
    font-size: small;
    line-height: 22px;
    margin: 0px 0;
    overflow: hidden;
    text-align: center;
}
.weather2 td {
    border-right: 2px solid #FFFFFF;
    padding: 5px 0px;
    vertical-align: top;
    width: 136px;
    border-right:1px solid #E7E7E7;
}
.weather2 td:last-child{border:none;}
.weather2 .last-child{border:none;}
.weathertoday {
    font-weight: bold;
}
.weathercite, .weathercite a {
    color: #666666;
}
.weashow {
    padding-top: 8px;
}
.weashow2 {
    display: none;
}
.weather2 .temp {
    font-family: Tahoma;
    font-size: 14px;
}
.weather2 p {
    line-height: 16px;
    margin: 0;
    padding: 5px 0 0;
}
.w_pic {
    height: 48px;
    margin: 3px 0;
    overflow: hidden;
}
.w_pic a {
    outline: medium none;
}
.w_pic img {
    margin: 0 3px;
}
.s_main{height:205px;padding-left:4px;}
.tdpt{padding:3px 0 0 0;*padding:6px 0 0 0;}
.tdpt2{padding:2px 0 0 0;*padding:5px 0 0 0;}
.tdpl{padding:0 9px 0 0;}
.fno{font-weight:normal; font-family:Arial;}
.linh20{line-height:20px;}
.mt3{margin-top:-3px;}
.smain1{line-height:1.5;margin:0 0 6px 0;}
#header .su_more a{color:#426BBD; text-decoration:underline;}
#header a.button3{width:111px;height:24px;line-height:24px;background:url(../images/img10.jpg) no-repeat;text-align:center;color:#fff;display:inline-block;margin:0px 10px 10px 0;}
#header a.button3_no{margin-right:0;}
#header a.button4{width:66px;height:24px;line-height:24px;background:url(../images/img12.jpg) no-repeat;text-align:center;color:#fff;display:inline-block;margin:5px 10px 10px 0;}
.safeauth_sugg {
    border: 1px solid #ccc;
    background-color: #fff;
    margin: 6px 0 0 4px;
    _margin: 5px 0 0 4px;
    padding: 0 10px;
    color: #999;
    position: absolute;
    line-height: 23px;
    font-weight:normal;
    white-space:nowrap;
    *top:8px;
    font-size: 12px;
    box-shadow: 1px 1px 2px rgba(0,0,0,.1);
    filter:progid:DXImageTransform.Microsoft.Shadow(Color=#dddddd, Strength=2, Direction=135),
}
.safeauth_sugg a {
    color: #369;
    display:inline-block;
    font-weight:normal;
    padding:0;
}
.vr_authico2{margin-left: 4px;_padding:0 0 0 4px;}

.s_title{color:#999;padding:5px 0 0 10px;}

.sugc_qna{overflow:hidden;padding:4px 0 0 8px; font-family:'ËÎÌå'}
.sugc_qna .qna_tit,.sug_jzwd .qna_tit{padding-bottom:8px;font-size:14px;color:#000;line-height:25px}
.sugc_qna .qna_tit a,.sug_jzwd .qna_tit a{color:#000 !important; display:block}
.sugc_qna .qna_tit a:hover,.sug_jzwd .qna_tit a:hover{background-color: #e5eaf3 !important;text-decoration: none !important}
.sugc_qna .qna_txt{margin-top:-5px;font-size:14px;line-height:22px}
.sugc_qna .qna_list{overflow:hidden;zoom:1;_padding-bottom:11px}
.sugc_qna .qna_list li{float:left;list-style:none}
.sugc_qna .wid50 li{width:49%}
.sugc_qna .wid33 li{width:33%;margin-bottom:11px}
.sugc_qna .im_qa{display:inline-block;overflow:hidden;margin-right:10px;padding:1px;border:1px solid #ccc;zoom:1}
.sugc_qna .im_lft{float:left}
.sugc_qna .im_qa img{vertical-align:top}
.sugc_qna .im_tit{display:block;overflow:hidden;width:90px;*width:84px;height:16px;margin-top:6px;white-space:nowrap;text-overflow:ellipsis;zoom:1}
.sugc_qna .wid33 .im_tit{width:74px;margin-top:5px;text-align:center}
.sugc_qna .im_tit a{line-height:16px}
.sugc_qna .im_tit a:hover{text-decoration:underline}
.sugc_qna .im_txt{display:block;overflow:hidden;color:#999}
.sugc_qna .qna_list_more{display:block;margin:-2px 0 0 4px}
.sugc_qna .qna_list_more a{color:#426bbd;text-decoration:underline}

.sugc_qna .sugc_qna_info{overflow:hidden;zoom:1}
.sugc_qna_v1{margin-left:-4px}
.sugc_qna .profile_thumb{overflow:hidden;float:left;width:65px;height:81px;margin-right:11px}
.sugc_qna .name_area{overflow:hidden;margin:1px 0 18px;line-height:22px}
.sugc_qna .name{font-size:14px;font-weight:bold}
.sugc_qna .name_type{font-size:12px;color:#999}
.sugc_qna .other_more{margin-right:12px}

.sugc_qna_info caption{display:none}
.sugc_qna .qna_tb{width:100%;margin-bottom:9px}
.sugc_qna .qna_tb th{height:21px;padding-left:10px;background-color:#eef2e8;line-height:20px;font-weight:normal;color:#646464;text-align:left}
.sugc_qna .qna_tb tr a{color:#0201cb}
.sugc_qna .qna_tb td{height:33px;padding-left:10px;border-bottom:1px dashed #e1e1e1;line-height:33px;color:#000}
.sugc_qna .qna_tb_more{display:block;margin-left:10px}
.sugc_qna .qna_tb_more a{color:#426bbd;text-decoration:underline}
.qna_tb tr a:hover,.other_more:hover{text-decoration:underline}

/*jzwd*/
.sug_jzwd{_display:inline;float:left;width:355px;height:264px;margin-left:17px;padding-top:12px;border-left:none}
.sug_jzwd em{color:#c00;font-weight:bold;font-style:normal}
.sug_jzwd a{text-decoration:none}
.sug_jzwd a:hover{text-decoration:underline}
.sug_jzwd_tit{margin-bottom:12px}
.jzwd_thumb{float:left;margin-right:10px}
.jzwd_thumb a{display:block;overflow:hidden;width:81px;height:81px}
.sug_jzwd_txt{color:#666;font-size:13px}
.sug_jzwd_tit{font-size:14px}
.sug_jzwd_tit2{padding:2px 0 6px;font-size:13px}
.sug_jzwd_txt span{margin-right:10px}
.sug_jzwd_txt2{padding-top:21px;color:#666;font-size:14px}
.sug_jzwd_txt2 em{color:#c00;font-weight:bold;font-style:normal}
.sug_jzwd_date{margin-left:10px;font-size:12px}
.sug_relation{overflow:hidden;zoom:1;padding-bottom:5px}
.sug_relation li{float:left;width:64px;height:105px;margin-left:65px;text-align:center;list-style:none}
.sug_relation .sug_relation_lst{margin-left:0}
.sug_relation .sug_relation_thumb{overflow:hidden;border:1px solid #d5d7de;zoom:1}
.sug_relation .sug_relation_thumb a{display:block;overflow:hidden;width:60px;height:60px;margin:1px}
.sug_relation li span{color:#999;font-size:12px}
.sug_relation .thumb_tit{padding:5px 0 2px}
.sug_relation_more{color:#426bbd !important;font-size:12px;text-decoration:underline !important}
.sug_relation_v1 .sug_relation_thumb a{height:86px}
.sug_relation_v1 li{height:147px;margin-left:21px}


/*train*/
.trainTable{width:100%;table-layout:auto;border-collapse:collapse;font-size:13px;}
.trainTable span{display:block;}
.iconStart{padding-left:}
.trainTable th{padding:0 5px;line-height:27px;background:-moz-linear-gradient(top,#f9f9f9,#eeeeee);background:-webkit-linear-gradient(top,#f9f9f9,#eeeeee);background:-o-linear-gradient(top,#f9f9f9,#eeeeee);filter:progid:DXImageTransform.Microsoft.gradient(startcolorstr=#F9F9F9, endcolorstr=#eeeeee, gradientType=0);font-weight:normal;padding:2px 5px;color:#666;border-bottom:1px solid #e2e2e2;text-align:left;}
.trainTable td{padding:7px 5px;vertical-align:top;background:#f9f9f9;border-top:1px dashed #e3e3e3;line-height:20px;}
.iconStart,.iconEnd{padding-left:20px;background:url(../images/icon_train.gif) no-repeat;}
.iconEnd{background-position:0 -35px;}
.infoBox{padding:0 31px 0 5px; line-height:30px;border-top:1px solid #e8e8e8;zoom:1;}
.infoBox a:hover{text-decoration:underline;}
.infoBox .aRight{float:right;}
.trainTable a:hover{text-decoration:underline;}

/*calendar*/
.suggestion .info{padding:0 5px;min-height:50px;}
.calendarBox{width:267px;min-height:50px;float:left;background:url(../images/calendarTitle.gif) no-repeat;}
.calendarBox ul{list-style-type:none;}
.calendarBox li{float:left;text-align:center;height:32px;float:left;width:38px;color:#fff;line-height:32px;}
.suggestion .info p{margin-bottom:8px;color:#666;}
.calendarBox table{border-width:0 1px 0 1px;border-style:solid;border-color:#e0e0e0;border-collapse:collapse;font-family:Arial;color:#333;}
.calendarBox table td{width:37px;border-width:0 1px 1px 0;border-color:#f0f0f0;background:#fefefe;height:28px;border-style:solid;text-align:center;font-size:11px;-webkit-text-size-adjust:none;}
.month{height:24px;border-bottom:1px solid #f0f0f0;border-left:1px solid #e0e0e0;border-right:1px solid #e0e0e0;border-width:0 1px 1px;line-height:24px;text-align:center;color:#666;}
.calendarBox .passed{color:#aaa;}
.line{height:1px;overflow:hidden;border:1px solid #e0e0e0;border-top:0;clear:both;}
.calendarBox table .no_tr_line td{border-bottom:1px solid #e0e0e0;}
.calendarBox .no_line{border-right:0;}
.calendarBox table .blueBg{background:#7cb5f0;color:#fff}
.calendarBox table .regBg{background:#f58189;color:#fff}
.calendarBox table .l_redBg{background:#f7949c;color:#fff;}
.calendarBox table span{font-family:SimSun;}
.calendarBox table{line-height:13px;}
.suggestion .msg{width:68px;float:left;margin-left:10px;padding-top:5px;}
.suggestion .clr{hiehgt:0;overflow:hidden;clear:both;}
.suggestion .msg span{width:12px;height:12px;overflow:hidden;background:#f58189;display:inline-block;vertical-align:middle;margin-right:5px;}
.suggestion .msg .blueBg span{background:#7cb5f0;}
.suggestion .msg .red span{background:#f58189;}
.suggestion .msg .red{color:#f58189}
.suggestion .msg .blueBg{color:#7cb5f0}


/*holiday*/
.holidayTatle{width:100%;color:#666;border-collapse:collapse;}
.holidayTatle th{background:#eef2e8;font-weight:normal;text-align:left;}
.holidayTatle th,.holidayTatle td{padding:0 5px;}
.holidayTatle th{line-height:21px;}
.holidayTatle td{line-height:28px;border-bottom:1px dashed #e3e3e3;}
.holidayTatle td a:hover{text-decoration:underline;}

/* kefu */
.su_service {
    border-collapse: collapse;
    margin-top: 2px;

}
.su_service td {
    vertical-align: top;
}
.su_servicepic {
    padding: 4px 10px 0 0;
    vertical-align: top;
}
.su_serviceinfo {
    line-height: 24px;
    white-space: nowrap;
    width: 270px;
}
.servicetxt {
    min-width: 7em;
    _width: 7em;
    color:#666;
    display: inline-block;
    white-space: nowrap;
}

/* new weather */
.weather{
    margin-left: 0;
    width: 414px;
    height:274px;
    background: #fff;
    border-left: 1px solid #f0f0f0;
    position: relative;
}
.weather .sugtype{padding-left: 8px;padding-right:10px;}
.weather .weather-list{
    overflow: hidden;
    margin-top: 15px;
    font-family: microsoft yahei;
    font-size: 13px;
}
.weather .weather-list li{
    float: left;
    list-style: none;
    border-right: 1px solid #f0f0f0;
    width: 92px;
    height: 187px;
    text-align: center;
}
.weather .weather-list .wlast{
    border: none;
}
.weather .weather-list .today{
    width: 115px;
    font-weight: bold;
}
.weather-list li .wimg{
    height: 68px;
    padding-top: 27px;
}
.weather-list .today .wimg{
    height: 79px;
    padding-top: 17px;
}
.weather .weather-list .temp,.weather .weather-list .forecast,.weather .weather-list .airpm{
    margin-top: 6px;
}
.weather .weather-list .airpm{
    margin-top: 2px;
}
.weather-list .show-air .temp{
    margin-top: 17px;
}
.weather .weather-list .temp{
    font-size: 13px;
}
.weather-list .today .temp{
    font-size: 15px;
}
.weather-list .today .forecast{
    margin-top: 5px;
}
.weather-list .airpm em{
    font-style: normal;
}
.airpm .g1{
    color: #7cc96d;
}
.airpm .g1{
    color: #7cc96d;
}
.airpm .g2{
    color: #f5c837;
}
.airpm .g3{
    color: #fd8a56;
}
.airpm .g4{
    color: #e84f56;
}
.airpm .g5{
    color: #a9386f;
}
.airpm .g6{
    color: #8a3743;
}
.weather .seven{
    position: absolute;
    bottom: 10px;
    right: 10px;
    text-align: right;
    color: #7777cc;
}
.forecast{line-height: 1.6em;}
.forecast span{
    display: block;
    overflow: hidden;
    word-wrap: normal;
    white-space: nowrap;
}
.suglist li .close{
    position: absolute;
    right: 5px;
    top: 5px;
    width: 16px;
    height: 16px;
    display: inline-block;
    background: url(../images/cico.png) no-repeat;
    _background:none;
    _filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src="../images/cico.png");
}
.suglist li .close:hover{
    background: url(../images/cico-h.png) no-repeat;
    _background:none;
    _filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src="../images/cico.png");
}

/* new sug moudel */
.mag-area{
    width:414px;
    margin:0;
    float:right;
    padding-top: 13px;
    border-left:1px solid #f0f0f0;
    overflow:hidden;
    height:262px;
    zoom: 1;
}
.sug-tit{
    font-size: 15px;
    line-height: 30px;
    position: relative;
}
.mag-area .sug-tit a{
    margin-left: 20px;
    padding:0;
    white-space: nowrap;
    overflow: hidden;
    -ms-text-overflow: ellipsis;
    text-overflow: ellipsis;
    word-wrap: normal;
    -ms-word-break: normal;
    word-break: normal;
}
.mag-area .sug-tit a img{
    vertical-align: -2px;
    margin-left: 4px;
}

.mag-area .sw-info{
    padding: 4px 0 0 20px;
    _padding-top:8px;
    zoom:1;
}
.mag-area .sw-info .resault-img,.hotmovie-img{
    float: left;
    border:1px solid #ebebeb;
    margin-right: 12px;
    opacity: 0.8;
    filter:alpha(opacity=80);
    position: relative;
}
.mag-area .sw-info .resault-img img{
    vertical-align: top;
}
.mag-area .sw-info .resault-img
.sw-info .sw-txt{
    overflow: hidden;
    overflow: inherit\9;
    margin-top: -2px\9;
    *margin-top: -3px;
    _margin-top: 0;
}
.sw-info .sw-txt h4{
    margin-bottom: 10px;
    color: #666;
    font-size: 13px;
}
.sw-info .sw-txt p{
    margin-bottom:7px;
    *margin-bottom: 5px;
    font-size: 13px;
    padding: 0;
}
.sw-info .sw-txt p img{
    margin-right: 5px;
    position: relative;
    top: -1px;
    border:none;
    vertical-align: top;
}
.sw-info .sw-txt p span{
    color: #666;
}
/* movie */
.hotmovie-txt{
    overflow: hidden;
    padding-top: 1px;
    padding-right: 20px;
}
.hotmovie-txt p{
    font-size: 13px;
    margin-bottom: 8px;
    *margin-bottom:3px;
    color: #333;
}
.hotmovie-txt2 p{
    margin-bottom: 6px;
}
.hotmovie-txt p span{
    color: #999;
}
.hotmovie-txt .hotmovie-time img{
    vertical-align: -3px;
    margin-right: 5px;
}
.hotmovie-douban,.hotmovie-prevue{
    margin-top: 13px;
}
.hotmovie-txt2 .hotmovie-prevue{
    margin-top: -2px;
    margin-bottom: 0;
}
.hotmovie-txt2 .hotmovie-douban{
    margin-top: 8px;
}
.hotmovie-douban{
    *margin-top: 6px;
}
.hotmovie-prevue{
    *margin-top: 10px;
}
.hotmovie-douban .vr-graphicMv-star {
    display: inline-block;
    width: 65px;
    height: 11px;
    overflow: hidden;
    background: url(../images/stars-tv.gif) repeat-x;
    *position: relative;
    *top: -3px;
}
.hotmovie-douban .vr-graphicMv-star span {
    display: block;
    height: 11px;
    overflow: hidden;
    background: #fff url(../images/stars-tv.gif) 0 -13px repeat-x;
    margin-right: 0;
}
.hotmovie-txt .hotmovie-douban em{
    color: #ef0000;
    margin-left: 5px;
    font-style: normal;
}
.hotmovie-type-l{
    margin-left: 30px;
}
.hotmovie-prevue a{
    display: inline-block;
    background: #5d9afc;
    border-radius: 2px;
    color: #fff;
    padding: 7px 10px;
}
.hotmovie-prevue a:hover{
    background: #518ded;
    text-decoration: none;
}
.hotmovie-prevue a img{
    vertical-align: text-bottom;
    margin-right: 5px;
}
.hotmovie-img{
    display: block;
    width: 120px;
    height: 160px;
    background: #000;
    overflow: hidden;
    opacity: 1;
    filter: alpha(opacity=100);
}
.hotmovie-img img{
    vertical-align: top;
}
.hotmovie-img:hover img{
    opacity: 0.5;
    filter: alpha(opacity=50);
}
.hotmovie-img .vr-mv-v2-play {
    display: none;
    width: 48px;
    height: 48px;
    overflow: hidden;
    position: absolute;
    top: 50%;
    left: 50%;
    margin-top: -24px;
    margin-left: -24px;
    background: url(../images/strPlay.png) no-repeat;
    _background: 0;
    _filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src=images/strPlay.png,sizingMethod=crop);
    z-index: 3;
    cursor: pointer;
}
.hotmovie-img:hover .vr-mv-v2-play{
    display: block;
}

.sugc .txt-link{margin-top: 10px;}
.sugc .txt-link a{
    display: inline-block;
    border-right: 1px solid #e4e4e4;
    padding-right: 10px;
    margin-right: 10px;
}
.sugc .txt-link .last-txtlink{
    padding: 0;
    margin: 0;
    border: none;
}
.mag-areabtn{
    *margin-top: 5px;
}
.mag-area .mag-areabtn a{
    display: inline-block;
    height: 26px;
    line-height: 26px;
    background: #5d9afc;
    color: #fff;
    padding: 0 12px;
    border-radius: 2px;
    margin-right: 8px;
    text-decoration: none;
}
.mag-areabtn a:hover{
    background: #518ded;
}
.mag-area .mag-areabtn .lastbtn{margin: 0;}

.hotmovie-info .hotmovie-infotit{
    float: left;
}
.hotmovie-info .hotmovie-infotxt{
    overflow: hidden;
    display: block;
    color: #333;
    line-height: 1.4em;
}
/* liebiao */
.modules-statistics h4{
    font-size: 13px;
    color: #666;
    line-height: 22px;
}
.sug-geshou .modules-statistics table{
    width: 100%;
    border-top: 0;
    margin: 0px 0 5px; table-layout:fixed; border-bottom:1px solid #f2f2f2; padding-top:2px
}
.modules-statistics td a{ text-decoration:none;}
.modules-statistics td img{ vertical-align:top}
.modules-statistics td a:hover{ text-decoration: underline}
.sug-geshou .modules-statistics table th,.sug-geshou .modules-statistics table td{
    border-bottom: 1px solid #f2f2f2;
    text-align: left;
    padding: 4px 0 4px; font-size:13px; line-height:18px
}
.modules-statistics td a .ico-new{ margin-left:5px; position:relative; top:1px}
.sug-geshou .modules-statistics table th{
    font-weight: normal; border-top:1px solid #f2f2f2;
    color: #999;
}
.tab-vr-tit-b6{ margin-bottom:-6px; position:relative}
.sug-geshou .modules-statistics table td{ padding-right:18px; border-bottom:0}
.suggestion .over-txt{overflow:hidden;white-space:nowrap;text-overflow:ellipsis;word-wrap:normal; margin-right:5px;width:100%}
.suggestion .moreLink{ position: relative; color:#999; line-height:20px}
.suggestion .moreLink span{ position:absolute; right:0; color:#999; top:0;}
.suggestion .moreLink a{text-decoration: none;margin-right: 8px;display: inline-block;color: #7777cc;font-family: sans-serif;}

/* music */
.sug-geshou{ padding:0 20px;}
.suggestion .tab-vr-tit{height:30px;background: url(../images/skin_x.gif) repeat-x 0 -13px;margin-top:3px;zoom: 1; position:relative}
.suggestion .tab-vr-tit:after,.suggestion .tab-vr-lis:after{display:block;content:'';clear:both;}
.suggestion .tab-vr-lis{zoom: 1; list-style:none}
.suggestion .tab-vr-lis li{float:left;zoom: 1;}
.suggestion .tab-vr-lis a{height:28px;line-height:28px;padding:1px 10px 0;_padding:1px 5px 0;font-size:13px;letter-spacing:normal;text-decoration:none;border-bottom:1px solid #e4e4e4;background:none;display:inline-block;color:#666;text-align:center;zoom: 1;}
.suggestion .tab-vr-lis .cur a{border:1px solid #e4e4e4;padding:0 10px;border-bottom:1px solid #FFF;background:#FFF;font-weight:bold;color:#333;cursor: default;}
.suggestion .tab-vr-lis .cur a:hover{text-decoration: none;}
.suggestion .tab-vr-tit .more{ position:absolute; right:0; top:0;font-family: sans-serif; line-height:30px}
.sub-gs-box{ overflow:hidden; zoom:1}
.sub-gs-box span{ float:left; margin-right:30px; line-height:20px; color:#999}
.sub-gs-box a{ display:inline-block}
.sub-gs-box .s1 a{ padding-left:23px; background:url(../images/ico-play2.gif) 0 0 no-repeat}
.sub-gs-box2{ height:177px; overflow-x:hidden; overflow-y:auto; border:1px solid #e4e4e4; padding:10px; font-size:13px; color:#333; line-height:20px; margin-top:5px}
.sub-gs-box2 p{ margin-bottom:20px}

.geshou-box,.geshou-box-txt{ overflow:hidden;zoom:1}
.geshou-box-img{ float:left; margin-right:10px; width:90px; height:90px; overflow:hidden; border:1px solid #ebebeb; margin-top:3px}
.geshou-box-txt{ line-height:20px; color:#999; font-size:13px;}
.geshou-box-txt .p1{ font-size:14px; font-weight:bold; padding-bottom:3px}
.geshou-box-txt .p3{ margin-top:5px}
.geshou-box-txt .p3 a{ padding-left:24px; display:inline-block; vertical-align:top}
.geshou-box-txt .p3 .a1{ background:url(../images/ico-play2.gif) 0 0 no-repeat}
.geshou-box-txt .p3 .a2{ background:url(../images/ico-down.gif) 0 0 no-repeat}
.geshou-box-txt .p3 .a3{ background:url(../images/ico-gc.gif) 0 0 no-repeat}
.geshou-box-txt .p3 span{ display:inline-block; width:1px; height:14px; background:#e4e4e4; margin:0 8px; vertical-align:top; position:relative; top:2px}

/* vacation */
.sug-vacation{
    padding: 0 20px 10px;
}
.sug-vacation .vac-overview,
.sug-vacation .vac-calendar{
    border-top: 1px solid #f2f2f2;
    border-bottom: 1px solid #f2f2f2;
    width: 100%;
    border-collapse: separate;
}
.sug-vacation .vac-overview th,
.sug-vacation .vac-calendar th{
    border-bottom: 1px solid #f2f2f2;
    padding: 4px 0 7px;
    color: #999999;
    font-weight: normal;
    text-align: left;
}
.sug-vacation .vac-overview td{
    padding: 4px 0 3px;
}
@-moz-document url-prefix() { .sug-vacation .vac-overview td { padding: 3px 0 3px; } }
.sug-vacation .vac-overview td a,
.sug-vacation .vac-overview td p{
    display: block;
    overflow: hidden;
    word-wrap: normal;
    word-break: normal;
    -webkit-text-overflow: ellipsis;
    text-overflow: ellipsis;
    white-space: nowrap;
}
.sug-vacation .vacation-more{
    margin-top: 5px;
    color: #7777cc;
    display: inline-block;
}
.sug-vacation .vac-calendar th{
    text-align: center;
    font-weight: bold;
}
.sug-vacation .vac-calendar td{

}
.sug-vacation .vac-calendar .vac-date{
    display: inline-block;
    width: 50px;
    height: 29px;
    padding: 11px 0 10px;
    text-align: center;
    position: relative;
}
.vac-calendar .vac-date .vac-solar{
    display: block;
    color: #bebebe;
    font-size: 16px;
    height: 20px;
}
.vac-calendar .vac-date .vac-lunar{
    display: block;
    color: #bebebe;
    font-size: 12px;
}
.vac-calendar .vac-date-beon{
    background: #e8f1ff;
}
.vac-calendar .vac-date-beon .vac-solar,
.vac-calendar .vac-date-beon .vac-lunar{
    color: #7eaefb;
}
.vac-calendar .vac-date .vac-beon{
    position: absolute;
    left: 0;
    top: 0;
    display: block;
    width: 15px;
    height: 15px;
    color: #fff;
    background: #9fc5ff;
    text-align: left;
    text-indent: 1px;
    line-height: 14px;
    overflow: hidden;
}
.vac-calendar .vac-date-rest{
    background: #ffeaef;
}
.vac-calendar .vac-date-rest .vac-solar,
.vac-calendar .vac-date-rest .vac-lunar{
    color: #f87777;
}
.vac-calendar .vac-date .vac-rest{
    background: #ffb4bd;
}
.sug-tit .sug-year{
    position: absolute;
    right: 20px;
    top: 0px;
    color: #999;
    font-size: 15px;
}
/* star-movie */
.sug-starmovie{
    padding: 0 20px 10px;
}
.sug-starmovie .sug-smlist{
    overflow: hidden;
    zoom: 1;
    margin: 10px -10px 10px 0;
}
.sug-starmsg .sug-smlist{
    margin-bottom: 8px;
}
.sug-starmovie .sug-smitem{
    float: left;
    margin-right: 10px;
    width: 82px;
    overflow: hidden;
    zoom: 1;
}
.sug-starmsg .sug-smitem{
    width: 108px;
}
.sug-starmovie .sug-smitem .sug-smpic{
    border: 1px solid #ebebeb;
    width: 80px;
    height: 106px;
    background: #000;
    display: block;
    position: relative;
}
.sug-starmsg .sug-smitem .sug-smpic{
    width: 106px;
    height: 80px;
}
.sug-starmsg .sug-smitem .sug-smpic:hover{
    text-decoration: none;
}
.sug-starmovie .sug-smitem .sug-smpic .sug-smplay{
    position: absolute;
    width: 40px;
    height: 40px;
    overflow: hidden;
    background: url(../images/agrrplay2.png) 0 0 no-repeat;
    _background: none;
    _filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src=images/agrrplay2.png, sizingMethod=crop);
    top: 50%;
    left: 50%;
    margin: -20px 0 0 -20px;
    display: none;
    cursor: pointer;
    z-index: 5;
}
.sug-starmovie .sug-smitem .sug-smpic:hover .sug-smplay{display: block;}
.sug-starmovie .sug-smitem .sug-smpic img{
    vertical-align: top;
}
.sug-starmsg .sug-smitem .sug-smpic:hover img {
    opacity: .5;
    filter: alpha(opacity=50);
}
.sug-starmovie .sug-smitem .sug-smpic:hover img{
    opacity: .5;
    filter:alpha(opacity=50);
}

.sug-starmovie .sug-smitem .sug-smpic .smitem-layer{
    display: block;
    width: 100%;
    height: 40px;
    background: url(../images/smbg.png) repeat-x;
    _background:none;
    _filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src="images/smbg.png");
    position: absolute;
    bottom: 0;
    left: 1px;
    right: 1px;
    z-index: 10;
}
.sug-starmovie .sug-smitem .sug-smpic .smitem-con{
    position: absolute;
    bottom: 5px;
    color: #fff;
    z-index: 11;
    width: 100%;
    zoom: 1;
}
.sug-starmovie .sug-smitem .sug-smpic .smitem-con .smitem-from{
    float: left;
    padding-left: 5px;
}
.sug-starmovie .sug-smitem .sug-smpic .smitem-con .smitem-time{
    float: right;
    padding-right: 5px;
}
.sug-starmovie .sug-smitem .sug-smtit{
    margin-top: 5px;
}
.sug-starmovie .sug-smitem .sug-smtit{
    margin-top: 5px;
}
.sug-starmsg .sug-smitem .sug-smtit{
    margin-top: 3px;
}
.sug-starmovie .sug-smitem .sug-smtit a{
    display: block;
    text-align: center;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    word-wrap: normal;
}
.sug-starmsg .sug-smitem .sug-smtit a{
    text-align: left;
    white-space: normal;
    line-height: 1.5em;
}

/* sugair */

.sugair .sugair-logo{
    width: 90px;
    height: 90px;
    display: block;
    border: 1px solid #ebebeb;
    border-radius: 1px;
    float: left;
    margin-right: 15px;
}
.sugair .sugair-adr{
    overflow: hidden;
    padding-top: 11px;
}
.sugair .sugair-adr .sugair-adrtxt{
    font-size: 15px;
    line-height: 15px;
}
.sugair .sugair-adr .sugair-adrtxt span{
    vertical-align: 2px;
    margin: 0 10px;
}
.sugair .sugair-adr .sugair-btn{
    display: block;
    width: 76px;
    height: 26px;
    line-height: 27px;
    font-size: 13px;
    border-radius: 2px;
    color: #fff;
    background-color: #5d9afc;
    text-align: center;
    margin-top: 14px;
}
.sugair .sugair-adr .sugair-btn:hover{
    background-color: #518ded;
    text-decoration: none;
}
.sugair .sugair-adrorder{
    padding-top: 0;
}
.sugair .sugair-adrorder .sugair-ordertxt{
    margin-top: -3px;
    line-height: 20px;
    color: #333;
}
.sugair .sugair-adrorder .sugair-ser{
    color: #666;
    line-height: 14px;
    margin-top: 6px;
}
.sugair .sugair-adrorder .sugair-ser img{
    vertical-align: -3px;
    margin-right: 2px;
}
.sugair .sugair-tip{
    font-size: 13px;
    line-height: 14px;
    color: #999;
    clear: both;
    margin-top: 10px;
    overflow: hidden;
}
/* history */

.sug-history{
    background: #fff;
    text-align: right;
    padding:0 10px;
    height: 28px;
    line-height: 28px;
    border-top: 1px solid #f3f3f3;
    color: #e4e4e4;
}
.sug-history：after{display:block;visibility:hidden;clear:both;height:0;content:'.'}
.sug-history span{
    margin: 0 10px;
}
.sug-history a{
    color: #777;
    text-decoration: none;
}
.sug-history a:hover{
    text-decoration: underline;
}
