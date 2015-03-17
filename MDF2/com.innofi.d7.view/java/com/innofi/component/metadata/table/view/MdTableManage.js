/**
 * @View
 * @Global
 */
function check(){

  var file = document.getElementById("file1").value;
  var fileEx=file.substring(file.lastIndexOf(".")+1,file.length);
  if(file == ""||fileEx!="xls"){
      dorado.MessageBox.alert('请选择要上传的xls模版文件');
      return false;
     }
  else{
      window.returnValue=file;
      return true;
     }

}