webpackJsonp([3],{"6XFZ":function(t,a,e){t.exports=e.p+"static/img/cost_1.81b8494.png"},"O+rh":function(t,a){},VCuI:function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var s=e("P9l9"),i=e("/4WC"),l=e.n(i),r={name:"costDataRecord",data:function(){return{userperadmin:{},appList:[],fileData:[],fileArray:[],loading:!1,allFiles:[],page:0,uploading:!1,showImg:!1,showAllFilesFlag:!1,toogleText:"查看全部",costDataEntry:!1}},mounted:function(){for(var t=JSON.parse(unescape(sessionStorage.getItem("userPermiss"))),a=0;a<t.data.length;a++)if("04"==t.data[a].meta)for(var e=0;e<t.data[a].children.length;e++)if("0401"==t.data[a].children[e].meta)for(var s=0;s<t.data[a].children[e].children.length;s++)"040101"==t.data[a].children[e].children[s].meta&&(this.costDataEntry="0"!=t.data[a].children[e].children[s].show);this.userperadmin=JSON.parse(localStorage.getItem("userperadmin")),this.getAppArrayFun()},computed:{fileContent:function(){var t=[];if(this.showAllFilesFlag)t=this.allFiles;else{var a=0|this.page;if(0!==this.fileArray.length){var e=this.fileArray[a].list;t=this.fileDataFormatter(e)}}return t}},methods:{getAppArrayFun:function(){var t=this,a={sessionid:this.userperadmin.sessionid};Object(s.w)(a).then(function(a){0===a.data.status?t.appList=a.data.appList:1===a.data.status?t.$router.replace("/login"):t.toast(a.data.statusMsg,"error")}).catch(function(a){t.toast("服务器错误，请联系管理员","error")})},toggleImg:function(){this.showImg=!this.showImg},dateFormatter:function(t){var a,e,s,i=t.split("/");return 4===i[0].length||4===i[2].length?(a=i[0],e=("0"+i[1]).substr(-2,2),s=("0"+i[2]).substr(-2,2)):(a=("20"+i[2]).substr(-4,4),e=("0"+i[0]).substr(-2,2),s=("0"+i[1]).substr(-2,2)),a+"-"+e+"-"+s},fileDataFormatter:function(t){var a=[];for(var e in t){var s="渠道";void 0===t[e]["渠道"]&&(s="渠道名",void 0===t[e]["渠道名"]&&(s="渠道名称")),a.push({date:this.dateFormatter(t[e]["日期"]),appid:t[e].app,channelName:t[e][s],channel:t[e]["渠道号"],cost:void 0!==t[e]["成本金额"]?t[e]["成本金额"]:t[e]["成本"]})}return a},appFormatter:function(t){var a;return this.appList.forEach(function(e){e.name===t&&(a=e.id)}),a},clearFileContent:function(){var t=document.getElementById("fileHandle");this.fileArray=[],this.page=0,t.setAttribute("type","text"),t.setAttribute("type","file")},showAllFiles:function(){this.showAllFilesFlag?(this.showAllFilesFlag=!1,this.toogleText="查看全部"):(this.showAllFilesFlag=!0,this.toogleText="分页查看")},getFileContent:function(t){if(!this.loading){this.uploading=!1;var a=t.target.files,e=new FileReader,s=[],i=[],r=[],n=this;this.loading=!0;try{e.onload=function(t){try{var a=t.target.result,e=l.a.read(a,{type:"binary"})}catch(t){return n.loading=!1,void n.toast("文件类型不正确","error")}for(var o in e.Sheets)if(i=[],e.Sheets.hasOwnProperty(o)){e.Sheets[o]["!ref"],r=r.concat(l.a.utils.sheet_to_json(e.Sheets[o]));var c={id:o,list:i=i.concat(l.a.utils.sheet_to_json(e.Sheets[o]))};s.push(c)}n.fileData=i,n.fileArray=s,n.allFiles=n.fileDataFormatter(r),n.loading=!1},e.readAsBinaryString(a[0])}catch(t){this.loading=!1}}},uploadDataFun:function(){var t=this;if(!this.loading&&0!==this.allFiles.length){var a=this.allFiles.slice(0),e=this.appList;a.forEach(function(t){for(var a=0;a<e.length;a++)t.appid===e[a].name&&(t.appid=e[a].id)});var i={sessionid:this.userperadmin.sessionid,fileData:a};this.loading=!0,Object(s._9)(i).then(function(a){t.loading=!1,0===a.data.status?t.uploading=!0:1===a.data.status?t.$router.replace("/login"):t.toast(a.data.statusMsg,"error")}).catch(function(a){t.loading=!1,t.toast("服务器错误，请联系管理员","error")})}},toast:function(t,a){this.$message({type:a,message:t})}}},n={render:function(){var t=this,a=t.$createElement,s=t._self._c||a;return s("div",{staticClass:"page cost-data-record-page"},[t._m(0),t._v(" "),s("section",{staticClass:"action-wrapper"},[s("div",{directives:[{name:"show",rawName:"v-show",value:t.costDataEntry,expression:"costDataEntry"}],staticClass:"fileUploadBtn btn primary"},[t._m(1),t._v(" "),s("input",{staticClass:"upload",attrs:{id:"fileHandle",type:"file"},on:{change:t.getFileContent}})]),t._v(" "),s("button",{directives:[{name:"show",rawName:"v-show",value:t.costDataEntry,expression:"costDataEntry"}],staticClass:"btn error",on:{click:t.clearFileContent}},[s("i",{staticClass:"fa fa-close"}),t._v("删除预览")]),t._v(" "),s("button",{directives:[{name:"show",rawName:"v-show",value:t.costDataEntry,expression:"costDataEntry"}],staticClass:"btn success",on:{click:t.uploadDataFun}},[s("i",{staticClass:"fa fa-check"}),t._v("提交预览")]),t._v(" "),s("p",{staticClass:"tooltip"},[s("span",{staticClass:"start"},[t._v("注：")]),t._v("灰色"),s("i",{staticClass:"fa fa-check-circle",attrs:{"aria-hidden":"true"}}),t._v("表示预览状态，绿色"),s("i",{staticClass:"fa fa-check-circle success",attrs:{"aria-hidden":"true"}}),t._v("表示已经提交预览。每次上传预览时，上一次未提交的预览会被覆盖。"),s("span",{staticClass:"notice",on:{mouseover:t.toggleImg,mouseout:t.toggleImg}},[t._v("查看数据格式")])])]),t._v(" "),t.showImg?s("section",{staticClass:"zindextop"},[s("img",{attrs:{src:e("6XFZ"),alt:"提示"}})]):t._e(),t._v(" "),s("section",{directives:[{name:"loading",rawName:"v-loading",value:t.loading,expression:"loading"}],staticClass:"table-wrapper",attrs:{"element-loading-spinner":"el-icon-loading","element-loading-text":"正在提交"}},[t.fileArray.length>=1?s("div",{staticClass:"table-toolbar"},[t.showAllFilesFlag?t._e():s("label",[t._v("Excel sheet：")]),t._v(" "),t.showAllFilesFlag?t._e():s("el-select",{attrs:{clearable:"",filterable:"","allow-create":"","default-first-option":"",placeholder:"选择数据页"},model:{value:t.page,callback:function(a){t.page=a},expression:"page"}},t._l(t.fileArray,function(t,a){return s("el-option",{key:t.id,attrs:{label:t.id,value:a}})})),t._v(" "),s("button",{staticClass:"btn primary",on:{click:t.showAllFiles}},[t._v(t._s(t.toogleText))])],1):t._e(),t._v(" "),s("el-table",{staticStyle:{width:"100%"},attrs:{id:"fileTable",data:t.fileContent,height:"400px",border:""}},[s("el-table-column",{attrs:{type:"index",label:"序号",width:"80"}}),t._v(" "),s("el-table-column",{attrs:{prop:"date",label:"日期"}}),t._v(" "),s("el-table-column",{attrs:{prop:"appid",label:"app"}}),t._v(" "),s("el-table-column",{attrs:{prop:"channelName",label:"渠道"}}),t._v(" "),s("el-table-column",{attrs:{prop:"channel",label:"渠道号"}}),t._v(" "),s("el-table-column",{attrs:{prop:"cost",label:"成本金额"}}),t._v(" "),s("el-table-column",{attrs:{prop:"state",label:"上传状态"},scopedSlots:t._u([{key:"default",fn:function(a){return[s("i",{staticClass:"fa fa-check-circle",class:{success:t.uploading},attrs:{"aria-hidden":"true"}})]}}])})],1)],1)])},staticRenderFns:[function(){var t=this.$createElement,a=this._self._c||t;return a("div",{staticClass:"bread-nav"},[a("p",[this._v("当前位置：数据中心 > 成本数据录入")])])},function(){var t=this.$createElement,a=this._self._c||t;return a("span",{staticClass:"text"},[a("i",{staticClass:"fa fa-upload"}),this._v("上传预览")])}]},o=e("Z0/y")(r,n,!1,function(t){e("O+rh")},"data-v-6e0e2ea8",null);a.default=o.exports}});