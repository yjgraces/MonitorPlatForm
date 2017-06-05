jQuery.fn.extend({
	/*
	 * 限制文本框长度
	 */
	limitLength: function(len, echo ,showTotal){
	    return this.each(function(){
	        var obj = jQuery(this);
	        if(obj.is('input:text') || obj.is('textarea')){
	            var that = this;
	            var events = ['keyup','focus','blur'];
	            jQuery.each(events, function(i,n){
	                jQuery(that).bind(n, function(){
	                    if(this.value.length > len)
	                        this.value = this.value.substring(0, len);
	                    if(n=='keyup'){
	                        jQuery(this).scrollTop(jQuery(this).height());
	                    }
	                    if(echo){
	                        if(showTotal==true){
	                            jQuery(echo).html((this.value == jQuery(this).data('tooltip') ? 0 : this.value.length) + '/' + len);
	                        }else{
	                            jQuery(echo).html(len-(this.value == jQuery(this).data('tooltip') ? 0 : this.value.length));
	                        }

	                    }
	                });
	            });
	            obj.triggerHandler('blur');
	        }
	    });
	},
	serializeObj:function(){
	    var array = this.serializeArray();
	    var obj = {} ;
	    $.each(array, function(i, item){
	    	if (obj[item.name] !== undefined) {
                obj[item.name] += ","+this.value;
            } else {
                obj[item.name] = this.value || '';
            }
		});

		return obj ;
	}
});

jQuery.extend({
	limitNum : function (selector) {
		$( document.body).delegate(selector,'keydown',function(event){
			var code = event.keyCode;
			return (code>47&&code<58)||(code>95&&code<106)
				||code==8||code==37||code==46||code==39;
		}).delegate(selector,'paste',function(event){
			return false ;
		}).delegate(selector,'input',function(event){
		   //chrome   输入法禁止有问题
		   var val = $.trim($(this).val());
		   if(!/^[\d]$/.test(val)){
			   $(this).val(val.replace(/[\D]/g,''));
		   }
	   })	
	}
});