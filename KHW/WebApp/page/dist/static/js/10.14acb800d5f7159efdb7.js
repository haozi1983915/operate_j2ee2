webpackJsonp([10],{hLEd:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var l=a("P9l9"),s={data:function(){return{userloginInfo:null,divHearderInfo:!1,partner:"",choosePartner:[],product:"",chooseproduct:[],content:"",gitTime:[],tableStaus:!1,tableData:[],deleteVisible:!1,deleteData:[],addVisible:!1,fbsData:[{date:[],appId:"",companyId:"",cooperationContent:"",cost:"",remark:""}]}},mounted:function(){this.userloginInfo=JSON.parse(localStorage.getItem("userperadmin")),this.getSelections()},methods:{getSelections:function(){var e={sessionid:this.userloginInfo.sessionid};this.divHearderInfo=!0;var t=this;Object(l._0)(e).then(function(e){0===e.data.status&&(t.choosePartner=e.data.reportforms_admin,t.chooseproduct=e.data.appList,t.divHearderInfo=!1,t.searchInfo()),1===e.data.status&&t.$router.replace("/login")})},searchInfo:function(){this.tableStaus=!0;var e={sessionid:this.userloginInfo.sessionid,id:this.partner,appId:this.product,cooperationContent:this.content,minDate:this.gitTime&&this.gitTime.length>0?this.gitTime[0]:"",maxDate:this.gitTime&&this.gitTime.length>0?this.gitTime[1]:""},t=this;Object(l.H)(e).then(function(e){0===e.data.status&&(t.tableData=e.data.reportforms_admin,t.tableStaus=!1),1===e.data.status&&t.$router.replace("/login")})},focuschoosePartner:function(){this.partner=""},focusDataTime:function(){this.gitTime=[]},addInfoToModel:function(){this.addVisible=!0,this.fbsData=[{date:[],appId:"",companyId:"",cooperationContent:"",cost:"",remark:""}]},deleteTableRow:function(e,t){this.deleteData=[e],this.deleteVisible=!0},deleteCost:function(){var e={sessionid:this.userloginInfo.sessionid,ruleForm:this.deleteData},t=this;Object(l.n)(e).then(function(e){0===e.data.status&&(t.$message({message:"删除数据成功",type:"success"}),t.deleteVisible=!1,t.searchInfo()),1===e.data.status&&t.$router.replace("/login")})},handleSelectionChange:function(e){this.deleteData=e},addArrPush:function(){this.fbsData.push({date:[],appId:"",companyId:"",cooperationContent:"",cost:"",remark:""})},deleteRow:function(e,t){this.fbsData.splice(t,1)},modelSubmit:function(){for(var e=0;e<this.fbsData.length;e++){if(!(this.fbsData[e].date&&this.fbsData[e].appId&&this.fbsData[e].companyId&&this.fbsData[e].cooperationContent&&this.fbsData[e].cost))return void this.$message.error("日期/产品/合作方/合作内容/成本金额 为必填项");this.fbsData[e].dateNew=this.fbsData[e].date[0]+"~"+this.fbsData[e].date[1]}var t={sessionid:this.userloginInfo.sessionid,ruleForm:this.fbsData},a=this;Object(l.e)(t).then(function(e){0===e.data.status&&(a.$message({message:"新增数据成功",type:"success"}),a.addVisible=!1,a.searchInfo()),1===e.data.status&&a.$router.replace("/login")})}}},o={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("div",{directives:[{name:"loading",rawName:"v-loading",value:e.divHearderInfo,expression:"divHearderInfo"}],staticClass:"hearder",attrs:{"element-loading-text":"拼命加载中..."}},[a("el-row",{attrs:{span:24}},[a("el-col",{attrs:{span:5}},[a("el-select",{staticClass:"templeMaring",attrs:{filterable:"",clearable:"","allow-create":"","default-first-option":"",size:"small",placeholder:"输入/选择合作方"},on:{focus:e.focuschoosePartner},model:{value:e.partner,callback:function(t){e.partner=t},expression:"partner"}},e._l(e.choosePartner,function(e,t){return a("el-option",{key:t,attrs:{label:e.company,value:e.id}})}))],1),e._v(" "),a("el-col",{attrs:{span:4}},[a("el-select",{staticClass:"templeMaring",attrs:{clearable:"",size:"small",placeholder:"选择产品"},model:{value:e.product,callback:function(t){e.product=t},expression:"product"}},e._l(e.chooseproduct,function(e,t){return a("el-option",{key:t,attrs:{label:e.name,value:e.id}})}))],1),e._v(" "),a("el-col",{attrs:{span:4}},[a("el-input",{staticClass:"templeMaring",attrs:{placeholder:"输入合作内容",clearable:"",size:"small"},model:{value:e.content,callback:function(t){e.content=t},expression:"content"}})],1),e._v(" "),a("el-col",{staticStyle:{"margin-left":"1rem"},attrs:{span:3}},[a("div",{staticClass:"block"},[a("el-date-picker",{attrs:{type:"daterange","start-placeholder":"开始日期","end-placeholder":"结束日期","unlink-panels":"","value-format":"yyyy-MM-dd",editable:!1,size:"small"},on:{focus:e.focusDataTime},model:{value:e.gitTime,callback:function(t){e.gitTime=t},expression:"gitTime"}})],1)])],1),e._v(" "),a("el-row",{staticClass:"headerSecond",attrs:{span:24}},[a("el-col",{attrs:{span:2}},[a("el-button",{attrs:{type:"primary",size:"small"},on:{click:e.deleteCost}},[e._v("一键删除")])],1),e._v(" "),a("el-col",{staticClass:"marginLeft",attrs:{span:4}},[a("el-button",{attrs:{type:"success",icon:"el-icon-plus",size:"small"},on:{click:e.addInfoToModel}}),e._v(" "),a("el-button",{attrs:{type:"primary",icon:"el-icon-search",size:"small"},on:{click:e.searchInfo}})],1)],1),e._v(" "),a("div",{directives:[{name:"loading",rawName:"v-loading",value:e.tableStaus,expression:"tableStaus"}],staticClass:"table",attrs:{"element-loading-text":"数据加载中"}},[a("el-table",{ref:"multipleTable",staticStyle:{width:"100%"},attrs:{size:"small",data:e.tableData,"tooltip-effect":"dark",border:"",height:e.tableData&&e.tableData.length>0?480:120},on:{"selection-change":e.handleSelectionChange}},[a("el-table-column",{attrs:{type:"selection",width:"55"}}),e._v(" "),a("el-table-column",{attrs:{prop:"dateNew",label:"日期",width:"200"}}),e._v(" "),a("el-table-column",{attrs:{prop:"name",label:"产品"}}),e._v(" "),a("el-table-column",{attrs:{prop:"company",label:"合作方"}}),e._v(" "),a("el-table-column",{attrs:{prop:"cooperationContent",label:"合作内容"}}),e._v(" "),a("el-table-column",{attrs:{prop:"cost",label:"成本金额"}}),e._v(" "),a("el-table-column",{attrs:{prop:"remark",label:"备注"}}),e._v(" "),a("el-table-column",{attrs:{label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.deleteTableRow(t.row,t.$index)}}},[a("i",{staticClass:"el-icon-delete"})])]}}])})],1)],1)],1),e._v(" "),a("div",{staticClass:"model"},[a("el-dialog",{attrs:{title:"增加",visible:e.addVisible,width:"90%"},on:{"update:visible":function(t){e.addVisible=t}}},[a("div",{staticClass:"addModel"},[a("el-button",{attrs:{type:"success",icon:"el-icon-plus",size:"small"},on:{click:e.addArrPush}})],1),e._v(" "),a("el-table",{attrs:{data:e.fbsData,height:e.fbsData&&e.fbsData.length>0?300:120,border:"",size:"small","highlight-current-row":!0}},[a("el-table-column",{attrs:{type:"index",label:"序号"}}),e._v(" "),a("el-table-column",{attrs:{prop:"date",label:"日期","min-width":"300"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("div",{staticClass:"block"},[a("el-date-picker",{staticClass:"dataStyleDate",attrs:{type:"daterange","start-placeholder":"开始日期","end-placeholder":"结束日期","unlink-panels":"","value-format":"yyyy-MM-dd",editable:!1,size:"small"},model:{value:t.row.date,callback:function(a){e.$set(t.row,"date",a)},expression:"scope.row.date"}})],1)]}}])}),e._v(" "),a("el-table-column",{attrs:{prop:"appId",label:"产品","min-width":"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-select",{attrs:{clearable:"",size:"mini",placeholder:"选择产品"},model:{value:t.row.appId,callback:function(a){e.$set(t.row,"appId",a)},expression:"scope.row.appId"}},e._l(e.chooseproduct,function(e,t){return a("el-option",{key:t,staticClass:"dataStyle",attrs:{label:e.name,value:e.id}})}))]}}])}),e._v(" "),a("el-table-column",{attrs:{prop:"companyId",label:"合作方","min-width":"300"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-select",{staticClass:"dataStyleDate",attrs:{clearable:"",size:"mini",placeholder:"选择合作方"},model:{value:t.row.companyId,callback:function(a){e.$set(t.row,"companyId",a)},expression:"scope.row.companyId"}},e._l(e.choosePartner,function(e,t){return a("el-option",{key:t,attrs:{label:e.company,value:e.id}})}))]}}])}),e._v(" "),a("el-table-column",{attrs:{prop:"cooperationContent",label:"合作内容","min-width":"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-input",{attrs:{clearable:"",size:"mini"},model:{value:t.row.cooperationContent,callback:function(a){e.$set(t.row,"cooperationContent",a)},expression:"scope.row.cooperationContent"}})]}}])}),e._v(" "),a("el-table-column",{attrs:{prop:"cost",label:"成本金额","min-width":"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-input",{attrs:{clearable:"",size:"mini"},model:{value:t.row.cost,callback:function(a){e.$set(t.row,"cost",a)},expression:"scope.row.cost"}})]}}])}),e._v(" "),a("el-table-column",{attrs:{prop:"remark",label:"备注","min-width":"200"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-input",{attrs:{clearable:"",size:"mini"},model:{value:t.row.remark,callback:function(a){e.$set(t.row,"remark",a)},expression:"scope.row.remark"}})]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.deleteRow(t.row,t.$index)}}},[a("i",{staticClass:"el-icon-delete"}),e._v("删除")])]}}])})],1),e._v(" "),a("span",{attrs:{slot:"footer"},slot:"footer"},[a("el-button",{attrs:{type:"primary",size:"small"},on:{click:e.modelSubmit}},[e._v("提交")])],1)],1)],1),e._v(" "),a("div",{staticClass:"model"},[a("el-dialog",{attrs:{title:"是否删除 ?",visible:e.deleteVisible,width:"60%"},on:{"update:visible":function(t){e.deleteVisible=t}}},[a("el-table",{staticStyle:{width:"100%"},attrs:{data:e.deleteData,border:"",size:"mini"}},[a("el-table-column",{attrs:{prop:"dateNew",label:"日期",width:"200"}}),e._v(" "),a("el-table-column",{attrs:{prop:"name",label:"产品"}}),e._v(" "),a("el-table-column",{attrs:{prop:"company",label:"合作方"}}),e._v(" "),a("el-table-column",{attrs:{prop:"cooperationContent",label:"合作内容"}}),e._v(" "),a("el-table-column",{attrs:{prop:"cost",label:"成本金额"}}),e._v(" "),a("el-table-column",{attrs:{prop:"remark",label:"备注"}})],1),e._v(" "),a("div",{staticStyle:{"padding-top":"20px"}},[a("span",[e._v('删除后不可恢复。点击"确认"进行删除，点击"x"关闭窗口')])]),e._v(" "),a("span",{attrs:{slot:"footer"},slot:"footer"},[a("el-button",{attrs:{type:"primary",size:"small"},on:{click:e.deleteCost}},[e._v("确定")])],1)],1)],1)])},staticRenderFns:[]},n=a("Z0/y")(s,o,!1,function(e){a("xfZZ")},"data-v-14a24705",null);t.default=n.exports},xfZZ:function(e,t){}});