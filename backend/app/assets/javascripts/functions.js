$(document).ready(function() {
	i = $('input :first').val();
	if(i == 'Listing users') {
		$('#text_nav_bar').text(i);
		$('#index_users').attr('class','active');
	}
	else if(i == 'Editing user') {
		$('#text_nav_bar').text(i);
		$('#index_users').attr('class','active');
		$('input :last').attr('class','btn btn-primary');
	}
	else if(i == 'Show user') {
		$('#text_nav_bar').text(i);
		$('#index_users').attr('class','active');
	}
	else if(i == 'New user') {
		$('#text_nav_bar').text(i);
		$('#new_user').attr('class','active');
		$('input :last').attr('class','btn btn-primary');
	}
	else if(i == 'Listing tasks') {
		$('#text_nav_bar').text(i);
		$('#index_tasks').attr('class','active');
	}
	else if(i == 'Editing task') {
		$('#text_nav_bar').text(i);
		$('#index_tasks').attr('class','active');
		$('input :last').attr('class','btn btn-primary');
	}
	else if(i == 'Show task') {
		$('#text_nav_bar').text(i);
		$('#index_tasks').attr('class','active');
	}
	else if(i == 'New task') {
		$('#text_nav_bar').text(i);
		$('#new_task').attr('class','active');
		$('input :last').attr('class','btn btn-primary');
	}
});