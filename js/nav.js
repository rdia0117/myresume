$(document).ready(function() {
	var flag=0;
		$(document).delegate('.open', 'click', function(event){
			$(this).addClass('oppenned');
			event.stopPropagation();
			flag=1;
		})
		
		$(document).delegate('body', 'click', function(event) {
			$('.open').removeClass('oppenned');
			flag=0;
		})
		$(document).delegate('.cls', 'click', function(event){
			if(flag==1){
				$('.open').removeClass('oppenned');
				event.stopPropagation();
				flag=0;
			}
		})
		$(document).delegate('.oppenned', 'click', function(event) {
			$('.open').removeClass('oppenned');
			flag=0;
		});

	});