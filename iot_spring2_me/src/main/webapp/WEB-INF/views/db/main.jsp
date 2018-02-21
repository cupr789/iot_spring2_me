<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<style>
html, body {
	width: 100%; /*provides the correct work of a full-screen layout*/
	height: 100%; /*provides the correct work of a full-screen layout*/
	overflow: hidden; /*hides the default body's space*/
	margin: 0px; /*hides the body's scrolls*/
}

div.controls {
	margin: 0px 10px;
	font-size: 14px;
	font-family: Tahoma;
	color: #404040;
	height: 80px;
}

.my_ftr {
	background-color: white;
	padding-top: 9px;
}

.my_ftr .text {
	font-family: Roboto, Arial, Helvetica;
	font-size: 14px;
	color: #404040;
	padding: 5px 10px;
	height: 70px;
	border: 1px solid #dfdfdf;
	overflow: auto;
}

</style>
<script> 

var bodyLayout, dbTree,winF,popW; 
var aLay, bLay, cLay;
var bTabs, bTab1, bTab2, bTab3;
var cTabs;
var tableInfoGrid;
var logDiv;
var forDelTag = /<\/?\w+>/gi;
function columnListCB(res){
	if(res.errorMsg){
		alert(res.errorMsg);
		return;
	}
	if(res.list){	

		tableInfoGrid = bTabs.tabs("tableInfo").attachGrid();
		var columns = res.list[0];
		var headerStr = "";
		var colTypeStr = "";
		for(var key in columns){
			if(key=="id") continue;
			headerStr += key + ",";
			colTypeStr += "ro,";
		}
		headerStr = headerStr.substr(0, headerStr.length-1);
		colTypeStr = colTypeStr.substr(0, colTypeStr.length-1);
        tableInfoGrid.setColumnIds(headerStr);
		tableInfoGrid.setHeader(headerStr);
		tableInfoGrid.setColTypes(colTypeStr);
        tableInfoGrid.init();
		tableInfoGrid.parse({data:res.list},"js");
		console.log(res);
	}
	if(res.tdlist){
		tableDataGrid = bTabs.tabs("tableData").attachGrid();
		var columns = res.tdlist[0];
		var headerStr = "";
		var colTypeStr = "";
		for(var key in columns){
			if(key=="id") continue;
			headerStr += key + ",";
			colTypeStr += "ro,";
		}
		headerStr = headerStr.substr(0, headerStr.length-1);
		colTypeStr = colTypeStr.substr(0, colTypeStr.length-1);
		tableDataGrid.setColumnIds(headerStr);
		tableDataGrid.setHeader(headerStr);
		tableDataGrid.setColTypes(colTypeStr);
		tableDataGrid.init();
		tableDataGrid.parse({data:res.tdlist},"js");
		console.log(res.tdList);
	}
}
function connectionListCB(res){
	if(res.errorMsg){
		alert(res.errorMsg);
		return;
	}
	dbTree = aLay.attachTreeView({
	    items: res.list
	});
	dbTree.attachEvent("onDblClick",function(id){
		var level = dbTree.getLevel(id);
		if(level==2){
			var text = dbTree.getItemText(id);
			var au = new AjaxUtil("${root}/connection/tables/" + text + "/" + id,null,"get");
			au.send(tableListCB); 
		}else if(level==3){
			var pId= dbTree.getParentId(id);
			var dbName = dbTree.getItemText(pId);
			var tableName = dbTree.getUserData(id,"orgText");
			var au = new AjaxUtil("${root}/connection/columns/" + dbName + "/" + tableName,null,"get");
			au.send(columnListCB);
		} 
	});
}
function tableListCB(res){
	if(res.errorMsg){
		alert(res.errorMsg);
		return;
	}
	var parentId = res.parentId;
	var i=1;
	for(var table of res.list){
		var id = parentId + "_" + i++;
		var text = table.tableName;
		if(table.tableComment!=""){
			text += "[" + table.tableComment + "]";
		}
		text += ":"+ table.tableSize + "KB"; 
		dbTree.addItem(id, text, parentId);
		dbTree.setUserData(id,"orgText",table.tableName);
	}
	dbTree.openItem(parentId);
}


function addConnectionCB(loader,res){
	var res = JSON.parse(res);
	alert(res.msg);
}



function dbListCB(res){
	if(res.error){
		alert(res.error);
		return;
	}
	var parentId = res.parentId;
	for(var db of res.list){
		var id = db.id;
		var text = db.text;
		dbTree.addItem(id, text, parentId);
	}
	dbTree.openItem(parentId);
}


function queryResult(res){
	
	
	if(res.errorMsg){
		alert(res.errorMsg);
	}
	
	
	if(res.list[0]){	
		cTabs = cLay.attachTabbar();
		for(var idx in res.list){
			cTabs.addTab("a"+idx,"result",null,null,false,true);
			queryGrid = cTabs.tabs("a"+idx).attachGrid();
			//queryGrid = cLay.attachGrid();
			var columns = res.list[idx][0];
			var headerStr = "";
			var colTypeStr = "";
			var headerStyle=[];
			for(var key in columns){
				if(key=="id") continue;
				headerStr += key + ","; 
				colTypeStr += "ro,";
				headerStyle.push("color:skyblue;");
			}
			headerStr = headerStr.substr(0, headerStr.length-1);
			colTypeStr = colTypeStr.substr(0, colTypeStr.length-1);
			queryGrid.setColumnIds(headerStr);
			queryGrid.setHeader(headerStr,null,headerStyle);
			queryGrid.setColTypes(colTypeStr);
			queryGrid.init();
			queryGrid.parse({data:res.list[idx]},"js");
		}		
	}
	for(var index in res.strSql){
		document.getElementById("inDiv").innerHTML +="실행한 쿼리: "+res.strSql[index]+"<br>Effected rows : "+res.effectCnt[index]+" 찾은 행: "+res.listCnt[index]+" 지속시간 : "+(res.excuteTime*0.001)+"sec"+"<br>";
	}
	
	
	
}

dhtmlxEvent(window,"load",function(){
	bodyLayout = new dhtmlXLayoutObject(document.body,"3L");
	logDiv = bodyLayout.attachFooter("footDiv");
	aLay = bodyLayout.cells("a");
	aLay.setWidth(300);
	aLay.setText("Connection Info List");
	var aToolbar = aLay.attachToolbar();
	aToolbar.addButton("addcon",1,"add Connector");
	aToolbar.addButton("condb",2,"Connection");
	aToolbar.attachEvent("onClick",function(id){
		if(id=="condb"){
			var rowId =dbTree.getSelectedId();
			if(!rowId){
				alert("접속할 커넥션을 선택해주세요.");
				return;
			}
			var au = new AjaxUtil("${root}/connection/db_list/" + rowId,null,"get");
			au.send(dbListCB); 
		}else if(id=="addcon"){
			popW.show();
		}
	})
	var au = new AjaxUtil("${root}/connection/list",null,"get");
	au.send(connectionListCB); 
	

	bLay = bodyLayout.cells("b");
	bTabs = bLay.attachTabbar({
		align:"left",
		tabs:[
			{id:"tableInfo", text:"Table Info"},
			{id:"tableData", text:"Table Datas"},
			{id:"sql", text:"Run Sql", active:true}
		]
	});
	var sqlFormObj = [
		{type: "block", blockOffset: 10, list: [
			{type: "button", name:"runBtn",value: "실행"},
			{type: "newcolumn"},
			{type: "button", name:"cancelBtn",value: "취소"} 
		]},
		{type:"input",name:"sqlTa",label:"sql",required:true,rows:10,style:"background-color:#ecf3f9;border:1px solid #39c;width:800"},
	];
	
	
	var sqlForm = bTabs.tabs("sql").attachEditor();
	
	
/* 	sqlForm.attachEvent("onButtonClick",function(name){
		
		var sendSQL = sqlForm.getItemValue("sqlTa");
		//var au = new AjaxUtil("${root}/sql/sendSql/" + sendSQL,null,"POST");
		
		sqlForm.send("${root}/sql/sendSql","POST",queryResult);
		
	}) */
	sqlForm.attachEvent("onAccess",function(evName,evObj){
		 if(evName=="keydown"){
		
				if(evObj.which==120 && evObj.ctrlKey){
					var sendSQL = sqlForm.getContent().replace(forDelTag," ");
					alert(sendSQL);
					var au = new AjaxUtilCustom("${root}/sql/sendSql",sendSQL);
					au.send(queryResult);
				}
				//sqlForm.send("${root}/sql/sendSql","POST",queryResult);
				/* var sendSQL = sqlForm.getContent();
				alert(sendSQL); */
				/* var au = new AjaxUtil("${root}/sql/sendSql/" + sendSQL,null,"POST");
				au.send(queryResult); */
			
		} 
		
		
	})
	
	
	
	cLay = bodyLayout.cells("c");
	
	winF = new dhtmlXWindows();
	popW = winF.createWindow("win1",20,30,320,300);
	//popW.hide(); 
	popW.setText("Add Connection Info"); 
	var formObj = [
		        {type:"settings", offsetTop:12,name:"connectionInfo",labelAlign:"left"},
				{type:"input",name:"ciName", label:"커넥션이름",required:true},
				{type:"input",name:"ciUrl", label:"접속URL",required:true},
				{type:"input",name:"ciPort", label:"PORT번호",validate:"ValidInteger",required:true},
				{type:"input",name:"ciDatabase", label:"데이터베이스",required:true},
				{type:"input",name:"ciUser", label:"사용자ID",required:true},
				{type:"password",name:"ciPwd", label:"비밀번호",required:true},
				{type:"input",name:"ciEtc", label:"설명"},
				{type: "block", blockOffset: 0, list: [
					{type: "button", name:"saveBtn",value: "저장"},
					{type: "newcolumn"},
					{type: "button", name:"cancelBtn",value: "취소"}
				]}
		];
	var form = popW.attachForm(formObj,true);
	popW.hide();
	
	form.attachEvent("onButtonClick",function(id){
		if(id=="saveBtn"){
			if(form.validate()){
				form.send("${root}/connection/insert", "post",addConnectionCB);
			}
		}else if(id=="cancelBtn"){
			form.clear();
		}
	});
	
})
</script>
<body>
	<div id="footDiv" class="my_ftr">
		<div id="inDiv" class="text"></div>
	</div>
</body>
</html>