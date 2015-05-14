window.onload =function(){
	var wholebody=document.getElementsByClassName("body");
	wholebody[0].className="";
	
	fadein(".sec0");
   	slideleft(".sec0");
    changePart();
    gray("app","100%","100%","100%","55%","22%","33%");
    gray("intro","52%","16%","25%","100%","100%","100%");
    gray("design","65%","35%","50%","100%","100%","100%");
};
function fadein(sec){
	fadeout(sec,"fadein");
	$(".fadein").fadeIn(1500);   
}
function fadeout(sec,dirct){
	var hide=document.getElementsByClassName(sec.substring(1)+" "+dirct);
	for(var i=0;i<hide.length;i++){
		hide[i].style.display="none";
	}
}
function slideleft(sec){
	$(sec+".slideleft").animate({right:80, opacity:"show"}, 0);
	fadeout(sec,"slideleft");
	$(sec+".slideleft").animate({right:0, opacity:"show"}, 1500);
}
function slideright(sec){
	$(sec+".slideright").animate({left:80, opacity:"show"}, 0);
	fadeout(sec,"slideright");
	$(sec+".slideright").animate({left:0, opacity:"show"}, 1500);
}
function slidetop(sec){
	$(sec+".slidetop").animate({bottom:80, opacity:"show"}, 0);
	fadeout(sec,"slidetop");
	$(sec+".slidetop").animate({bottom:0, opacity:"show"}, 1500);
}
function slidebottom(sec){
	$(sec+".slidebottom").animate({top:80, opacity:"show"}, 0);
	fadeout(sec,"slidebottom");
	$(sec+".slidebottom").animate({top:0, opacity:"show"}, 1500);
}
function changePart() {
	var vid = document.getElementsByTagName("video"); 
	var part =location.hash.substring(1,5)+location.hash.substring(6);
	var partclass="."+part;

    fadein(partclass);
    slideleft(partclass);
    slideright(partclass);
    slidetop(partclass);
    slidebottom(partclass);
    for(var i=0;i<vid.length;i++){
    	vid[i].pause();
    }

}
function gray(classname,maxheight,minheight,argheight,maxwidth,minwidth,argwidth){
	var flag=0;
	 $('.graysize.'+classname).click(function() {
	 	var subtitle=document.getElementsByClassName("subtitle");
	 	var itemlist=document.getElementsByClassName("graysize "+classname);
		var thisitem=document.getElementById(this.id);
	 	if(flag==0){
		 	for(var i=0;i<itemlist.length;i++){
		 		if((this.id)!=(itemlist[i].id)){
		 			chwidth("#"+itemlist[i].id,minheight,minwidth);
		 		}
		 	}
		 	chwidth("#"+this.id,maxheight,maxwidth);
		 	$(".subtitle."+this.id).fadeIn(1000);
		 	flag=1;
		}else if(flag==1){
			chwidth(".graysize."+classname,argheight,argwidth);
			for(var i=0;i<subtitle.length;i++){
		 		subtitle[i].style.display="none";
		 	}
	 		flag=0;
		}
	});
	 $(".grayhover."+classname).click(function() {
		var thisitem=document.getElementById(this.id);
	 	if(flag==0){
		 	thisitem.className=thisitem.className+"  brown";
		}
		else if(flag==1){
			var itemlist=document.getElementsByClassName("grayhover "+classname);
			for(var i=0;i<itemlist.length;i++){
		 		itemlist[i].className="grayhover gray "+classname;
		 	}
		 	thisitem.className="grayhover "+classname;
		}
	});
	$(".grayhover."+classname).mouseover(function(){
		var itemlist=document.getElementsByClassName("grayhover "+classname);
		var thisitem=document.getElementById(this.id);
		if(flag==0){
			for(var i=0;i<itemlist.length;i++){
		 		itemlist[i].className=itemlist[i].className+" gray";
		 	}
		 	thisitem.className="grayhover "+classname;
		}
	});
	$(".grayhover."+classname).mouseout(function(){
		var itemlist=document.getElementsByClassName("grayhover "+classname);
		if(flag==0){
			for(var i=0;i<itemlist.length;i++){
		 		itemlist[i].className="grayhover "+classname;
		 	}
		}
	});
}
function chwidth(item,newheight,newwidth){
	$(item).animate({height:newheight,width:newwidth,opacity:"show"},300);
}