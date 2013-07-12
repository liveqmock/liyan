function fileupload(filepath){  
	if(filepath!=""){     
		sendFileToServer(filepath);  
	}else{
		alert("请选择要上传的文件！");  
	}  
}   
function callback() {
	if (xmlhttp.readyState == 4) {
		var responseText = xmlhttp.responseText;             
	}  
}   
function sendByteStreamToServer(stream,url,filepath){
	if (window.XMLHttpRequest) {
		xmlhttp = new XMLHttpRequest();
		if (xmlhttp.overrideMimeType) {
			xmlhttp.overrideMimeType("text/xml");
		}   
	}else if (window.ActiveXObject) {
		var activexName = ["MSXML2.XMLHTTP","Microsoft.XMLHTTP"];
		for (var i = 0; i < activexName.length; i++) {
			try {
				xmlhttp = new ActiveXObject(activexName[i]);
				break;
			} catch(e) {
				e.print();           
			}       
		}
	}
	var upload_filename =  filepath ;
	var upload_filepath =  filepath ; 
	var byte_size = stream.Size ; 
	var byte_unit = 1024 ;
	var read_count = parseInt((byte_size/byte_unit).toString())+parseInt(((byte_size%byte_unit)==0)?0:1);
	// 创建XML对象，组合XML文档数据
    var xml_dom = new ActiveXObject("msxml2.domdocument");
    xml_dom.loadXML("<?xml version=\"1.0\" ?> <root/>");
	// 创建XML元素节点，保存上传文件名称
    var node = xml_dom.createElement("uploadfilename");
    node.text = upload_filename.toString();
    xml_dom.documentElement.appendChild(node);
    
    // 创建XML元素节点，保存上传文件路径
    var node = xml_dom.createElement("uploadfilepath");
    node.text = upload_filepath.toString();
    xml_dom.documentElement.appendChild(node);
    
    // 创建XML元素节点，保存上传文件大小
    var node = xml_dom.createElement("uploadfilesize");
    node.text = byte_size.toString();
    xml_dom.documentElement.appendChild(node);
    
    // 创建XML元素节点，保存上传文件内容
    for(var i=0;i<read_count;i++){
        var node = xml_dom.createElement("uploadcontent");
        // 文件内容编码方式为Base64
        node.dataType = "bin.base64";
        // 判断当前保存的数据节点大小，根据条件进行分类操作
        if((parseInt(byte_size%byte_unit)!=0)&&(i==parseInt(read_count-1))){
            // 当数据包大小不是数据单元的整数倍时，读取最后剩余的小于数据单元的所有数据
            node.nodeTypedValue = stream.Read();
        }else{
            // 读取一个完整数据单元的数据
            node.nodeTypedValue = stream.Read(byte_unit);
        }
        xml_dom.documentElement.appendChild(node);
    }
	xmlhttp.onreadystatechange = callback;  
	xmlhttp.open("post", url, false);
	boundary="abcd"  ;
	xmlhttp.setRequestHeader("Content-Type",  "multipart/form-data,boundary="+boundary);
	xmlhttp.setRequestHeader("Content-Length", stream.Size);
	//alert(stream.size);
	xmlhttp.send(xml_dom);
}  
function sendFileToServer(filePath){
	var stream =new ActiveXObject("ADODB.Stream");
	stream.Type=1;
	stream.Mode=3;
	stream.Open();
	stream.Position = 0;//指定或返加对像内数据的当前指针。
	stream.LoadFromFile(filePath); //将FileName指定的文件装入对像中,参数FileName为指定的用户名。
	stream.Position = 0;       
	sendByteStreamToServer(stream,_path+"/uploadservlet.do",filePath);
	stream.Close(); 
}  
var countfiles=0;
var countfolders=0;   //用于打开浏览对话框，选择路径 
function BrowseFolder(){ 
	try{    
		var Message = "请选择文件夹"; //选择框提示信息   
		var Shell = new ActiveXObject( "Shell.Application" );
		var Folder = Shell.BrowseForFolder(0,Message,0); //起始目录为：桌面    
		if(Folder != null){    
			Folder = Folder.items(); // 返回 FolderItems 对象   
			Folder = Folder.item(); // 返回 Folderitem 对象   
			Folder = Folder.Path; // 返回路径    
			if(Folder.charAt(Folder.length-1) != "\\"){
				Folder = Folder + "\\";    
			}
			return Folder; 
		}else{
				Folder=""; 
				return Folder;   
		} 
	}  catch(e){
		alert(e.message+"11"); 
	}
	
}    
//用于遍历 
function traverse(localPath,textHtml){
	var fso = new ActiveXObject("Scripting.FileSystemObject");
	var currentFolder = fso.GetFolder(localPath);
	var fileList = new Enumerator(currentFolder.files);
	var subFolderList = "";
	var fileHtml=textHtml;
	var aFile;
	for (; !fileList.atEnd(); fileList.moveNext())   {
		countfiles++;
		aFile=fileList.item();
		//fileHtml+="filename:"+aFile.Name.substring(0,aFile.Name.lastIndexOf("."))+","
		//fileHtml+="文件属性:"+aFile.Attributes+",";
		//fileHtml+="创建日期："+aFile.DateCreated+"<br/>";
		//fileHtml+="最后存取时间："+aFile.DateLastAccessed+"<br/>";
		//fileHtml+="最后修改时间："+aFile.DateLastModified+"<br/>";
		//fileHtml+="父目录："+aFile.ParentFolder+",";
		//fileHtml+="path:"+aFile.Path+",";
		//fileHtml+=aFile.Path;
		fileHtml.push(aFile.Path);
		//fileHtml+="短文件名:"+aFile.ShortName+",";
		//fileHtml+="短路径:"+aFile.ShortPath+",";
		//fileHtml+="size:"+aFile.Size+"<br>";
		//fileHtml+="type:"+aFile.Type +"<br/>";
		//fileHtml+="所在盘："+aFile.Drive+"<br/><hr>";
	}
	subFolderList = new Enumerator(currentFolder.SubFolders);
	for (; !subFolderList.atEnd(); subFolderList.moveNext())   {
		
		countfolders++;    
		//fileHtml +="chfile："+ subFolderList.item().Path+"<hr>";
		fileHtml=traverse(subFolderList.item().Path,fileHtml);//递归遍历子文件夹   
	}    
	//fileHtml+="共遍历文件数："+countfiles+"<br/>"+"共遍历文件夹数："+countfolders;
	return(fileHtml); 
}  
function browse(){
	document.all.path.value=BrowseFolder();
}  
function viewfiles(){
	//var textHtml="";
	var textHtml = new Array();
	var textHtmls = new Array();
	var folderpath=document.all.path.value;
	//document.getElementById("path").value=traverse(folderpath,textHtml);
	//span.innerHTML=traverse(folderpath,textHtml)+"共遍历文件数："+countfiles+"<br/>"+"共遍历文件夹数："+countfolders;
	textHtmls=traverse(folderpath,textHtml);
	var text="" ;
	for(var i=0;i<(textHtmls.length);i++){
		fileupload(textHtmls[i]);
		// alert(textHtmls[i]);
		text = text+textHtmls[i]+"<br/>"; 
	}
	document.getElementById("tt").innerHTML =text;
	return true;
}   
var XMLHttpReq;
var currentSort;
//创建XMLHttpRequest对象
function createXMLHttpRequest() {
	if(window.XMLHttpRequest) { //Mozilla 浏览器
		XMLHttpReq = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) { // IE浏览器
		try {
			XMLHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e) { 
                try {
                	XMLHttpReq  =  new  ActiveXObject("Microsoft.XMLHTTP");
                	} catch (e) {
                		
                	}
            }
		}
}    