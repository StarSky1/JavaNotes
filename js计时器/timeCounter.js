//计时器
var minutes=0;
var hours=0;
var seconds=0;
var i=setInterval(function(){
	if(seconds<60){
		console.log('不知不jiao，已经过去'+hours+"小时"+minutes+"分钟 "+seconds+"秒啦！微风吹过我的脸颊，好清凉。");
		seconds++;
	}else{
		seconds=0;
		minutes++;
		console.log("亲爱的主人，您已经阅读了"+hours+"小时"+minutes+"分钟哦！您真棒！");
	}
	if(minutes<60){
		//do nothing
	}else{
		minutes=0;
		hours++;
	}
	if(hours>4){
		clearInterval(i);
		console.log("亲爱的主人，您已经阅读了"+hours+"小时啦，休息一下吧！");	
	}
},1000);	//每一秒执行一次

