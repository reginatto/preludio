$(document).ready(function(){
 
 $('#main').validate(
 {
  rules: {
	 profile_full_name: {
      required: true
    },
    profile_email: {
      required: true,
      email: true
    },
    profile_password: {
      minlength: 8,
      required: true
    }
  },
  highlight: function(element) {
    $(element).closest('.control-group').removeClass('success').addClass('error');
  },
  success: function(element) {
    element
    .text('OK!').addClass('valid')
    .closest('.control-group').removeClass('error').addClass('success');
  }
 });
});