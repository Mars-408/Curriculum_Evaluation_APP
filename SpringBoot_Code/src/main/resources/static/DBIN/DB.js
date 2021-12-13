$("#button-import").upload({  
    action: '实际开发中的提交位置',  
    onSelect: function (self, element) {  
    this.autoSubmit = false;  //关闭自动提交,方便后面进行文件类型判断
    var filename=this.filename();//设定文件名
                var flag1= filename.endsWith(".xls");
                var flag2= filename.endsWith(".xlsx");
                if(flag1||flag2){//判断是否是所需要的文件类型,这里使用的是excel文件
                      this.submit();
                }else{
                      alert("提示请选择正确的文件");
                }
                        
                },  
       onComplete: function (data, self, element) {  
               alert("提示文件上传成功");//当上传成功后,提示
         } 
});