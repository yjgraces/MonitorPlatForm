var UI = {};
(function(UI){
	/**
	 * 遮罩效果
	 */
	var mask = null ;
	var maskLayer = null;
	var timer = null ;	
	var defaultOpts = {
		backgroundColor:'#000',
		opacity:0.5,
		display:'block',
		position:'absolute',
		top:0,
		left:0,
		zIndex:100
	}
	
	function getMaskLayer() {
		if(maskLayer == null) {
			maskLayer = $('<div id="maskLayer"><div class="maskpanel"></div></div>').appendTo(document.body);
		}
		return maskLayer;
	}
	
	function isVisible (){
		return getMaskLayer().css('display')!='none';
	}
	
	function bindEvent(){		
		// 可见状态需要resize
		$(window).resize(function(){
			if(isVisible()){
				clearTimeout(timer);
				timer = setTimeout(function(){
					//hide();
					show();	
				},100);
			}
		});
		
		// 注销方法 避免下次调用
		bindEvent = new Function();
	}
	
	var show = function(opt) {
		var opt = $.extend(defaultOpts,opt);
		getMaskLayer().css($.extend(opt,{
			width:$(document).width(), 
			height:$(document).height()
		})).show();
		
		//bindEvent();
	}	
	
	var hide = function() {
		getMaskLayer().hide();
	} 
	
	var remove = function(){
		getMaskLayer().remove() ;
	}
	
	UI.mask = {
		hide:hide,
		show:show,
		remove:remove
	}
})(UI);

(function(){
	function getScrollTop(){
	    return document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
	}
	function getScrollLeft(){
	    return document.documentElement.scrollLeft || window.pageXOffset || document.body.scrollLeft;
	}
	var Box = function (opts) {
	    var defaultOpts = {
	        cont: '',
	        btnContent:'',
	        customClass: '',
	        zIndex: 102,					//窗口层级
	        /*left: 0,             
	        top: 0,   */       
	        mask: {
	            color:'#000',
	            opacity:0.5  
	        },						//遮罩参数
	        modal: true,			//是否显示遮罩
	        drag: 0,				//是否能拖拽
	        timeout: 0,
	        fixed:true
	    }
	    $.extend(this, defaultOpts, opts);
	}
	$.extend(Box.prototype, {
	    init: function () {
	        var self = this;

	        if (!self.cont || self.cont.length == 0) {
	            var boxHtml = ['<div class="ui_pop_box">',
	                                (self.noheader?'':this.getHeader()),
	                                '<div class="ui_pop_body">' ,
	                                    '<div class="j-content"></div>' ,
	                                    (self.btnContent?'<div class="input_button_box j-buttons">'+self.btnContent+'</div>':'') ,
	                                '</div>' ,
	                            '</div>'].join('');  
	            self.cont = $('<div class="ui_window ' + self.customClass + '" style="visibility:visible;z-index:'+(self.zIndex )+'">'+boxHtml+'</div>');

	            $("body").append(self.cont);

	            if (self.height) {
	                self.cont.find(".ui_pop_body").css("height", self.height);
	            }
	        }
	        
	        if (self.keyDown != 1) {
	            self.keyDown();
	        }//ESC关闭层事件
	        self.Drag(self.drag);//拖拽
	        self.setContent();//填充内容

	        self.cont.find(".j-close").on("click", function () {//点击关闭按钮
	            self.hide();
	            self.onClose && self.onClose();
	        });
	        //resize
	        $(window).unbind('resize.dialog').bind('resize.dialog',function(){
	            clearTimeout(self.timer);
	            self.timer = setTimeout(function(){
	                self.show(true);    
	            },100); 
	        })
	    },
	    getHeader:function(){
	        var html = '' ;
	        if (this.title) {
	                html = [ '<div  class="ui_pop_head">',
	                            '<div class="ui_pop_head_tit">' + this.title + '</div>',
	                            (this.noclose ? '' : '<div class="ui_pop_head_btn_close j-close">X</div>'),
	                        '</div>'].join('') ;
	            }  
	        return  html ;      
	    },
	    show: function (isResize) {
	        var self = this;
	        self.resize().setPos();
	        self.cont.show();
	        self.modal && UI.mask.show($.extend({zIndex:self.zIndex-1},self.mask));
	        if(!isResize){
	            if (self.timeout != 0) {//是否自动消失
	                var timer = setTimeout(function () {
	                    self.hide();
	                }, self.timeout);
	            }

	            if ($.isFunction(self.onShow)) {
	                self.onShow.call(self, self);
	            }    
	        }
	    },

	    hide: function () {
	        var self = this;
	        self.cont.hide();
	        self.modal && UI.mask.hide();
	        $(document).off("mousemove");
	        $(document).off("mouseup");
	        if (self.onHide && $.isFunction(self.onHide)) {
	            self.onHide.call(self, self);
	        }
	    },
	    remove: function () {
	        this.cont.remove();
	        this.modal && UI.mask.remove();
	    },
	    keyDown: function () {
	        var self = this;
	        $(document).keydown(function (e) {
	            e = e || event;
	            if (e.keyCode == 27) {
	                self.hide();
	            }
	        })
	    },
	    resize: function () {
	        var cont = this.cont;
	        
	        this.height = cont.height();
	        return this;
	    },
	    setPos: function () {
	        var self = this,
	            cont = self.cont,
	            win = $(window);
	        var position = this.fixed?'fixed':'absolute';

	        var left = this.left || null;
	        var top = this.top || null;
	        
	        // 检查默认参数是否有效 如果无效采取居中策略
	        if(top === null) {
	            top = ($(window).height() - cont.outerHeight()) / 2;
	            // 如果不支持fixed 或者弹窗不是fix定位 需要加上滚动距离
	            // 滚动条距离在多个浏览器中刷新时会有计算延迟总是返回 0
	            // 如果弹窗需要在页面加载完成时立刻弹出就会有定位偏差
	            // 因此bindEvent中绑定了一次scroll事件以便重新定位
	            if(!this.fixed)
	                top += getScrollTop();
	            
	            top = Math.max(top, 0);
	        }
	        if(left === null) {
	            left = ($(window).width() - cont.outerWidth()) / 2;
	            // 如果不支持fixed 或者弹窗不是fix定位 需要加上滚动距离
	            if(!this.fixed)
	                left += getScrollLeft();
	            
	            left = Math.max(left, 0);
	        }
	        
	        cont.css({
	           "width": cont.outerWidth(),
	            "position": position,
	            "top": top,
	            "left": left
	        });
	        return this;
	    },
	    setContent: function () {
	        var self = this;
	        var contents = self.cont.find(".j-content");
	        var relcontent = self.content;
	        if (contents.length == 0) {
	            return;
	        }
	        if (typeof relcontent == "string") {
	            relcontent = $(relcontent);
	            contents.html(relcontent);
	        } else {
	            contents.append(relcontent.show());
	        }

	        this.cont.width(relcontent.outerWidth());
	    },
	    setBtnsContent: function () {
	        var self = this;
	        if (self.btnContent) {
	            this.cont.find('.j-buttons').html(self.btnContent);
	        }
	        return this;
	    },
	    Drag: function (ifDrag) {
	        if (ifDrag == 0) {
	            return;
	        }
	        var flag = !1, pageX, pageY, win = $(window);
	        var self = this, cont = self.cont, header = cont.find(".ui_pop_head");
	        header.bind("mousedown", function (e) {
	            $(".ui_pop_box:visible").length > 0 && (
	                flag = !0,
	                    pageX = e.pageX - parseInt(cont.css("left"), 10),
	                    pageY = e.pageY - parseInt(cont.css("top"), 10),
	                    header.css({
	                        "cursor": "move"
	                    })
	            );
	        });
	        $(document).bind("mousemove", function (e) {
	            if (flag && cont.length > 0 && ifDrag) {
	                cont.fadeTo(0, 1);
	                var dX = e.pageX - pageX,
	                    dY = e.pageY - pageY;
	                dX < 0 && (dX = 0),
	                dX > win.width() - cont.width() && (dX = win.width() - cont.width() - 2),
	                dY < 0 && (dY = 0), dY > win.height() - cont.height() && (dY = win.height() - cont.height() - 2),
	                    cont.css({
	                        "top": dY,
	                        "left": dX
	                    });
	            }
	        }).bind("mouseup", function () {
	            $(".ui_pop_box:visible").length > 0 && ifDrag && (flag = !1, cont.fadeTo(0, 1), header.css({
	                "cursor": "auto"
	            }));
	        });
	    }
	});
	UI.dialog = Box ;
})();

(function(UI){
	var systemerror = '系统错误，请稍后再试！';
	UI.confirm = function(params,cb){
		var params = $.extend({
			width:300,
			title:'确认提示',
			customClass:'confirm-dialog',
			btnContent:'<a class="js_ok" href="javascript:;">'+(params.oktxt||'确定')+'</a><a class="js_cancel" href="javascript:;">取消</a>'	
		},params);
		var dia =new UI.dialog(params);
		dia.init();
		dia.show();
		dia.cont.find('.js_ok').click(function(){
			dia.hide();
			cb&&cb();
		})
		dia.cont.find('.js_cancel').click(function(){
			dia.hide();
		})
	}
	UI.showMessage = function(msg){
		//if(typeof msg == ) 
		var dia =new UI.dialog({
			noclose:true,
			title:'温馨提示',
			timeout:3000,
			customClass:'message-dialog',
			content:'<div class="tip">'+msg+'</div>'
		});
		dia.init();
		dia.show();  
		return dia ;  
	}
	UI.alert = function(msg){
		//if(typeof msg == ) 
		var dia =new UI.dialog({
			noclose:false,
			title:'温馨提示',
			customClass:'alert-dialog',
			content:'<div class="tip">'+msg+'</div>',
			btnContent:'<a class="js_ok" href="javascript:;">确定</a>'
		});
		dia.init();
		dia.show(); 
		dia.cont.find('.js_ok').click(function(){
			dia.hide();
		}) 
		return dia ;  
	}
	UI.common = function(params){
		var content = params.box.content ;
		if (typeof content !== "string") {
             content = content.clone().appendTo('<div></div>').parent().html();
        } 
		var options = $.extend({
			title:'温馨提示',
			customClass:'common-dialog',
			btnContent:'<a class="js_ok" href="javascript:;">保存</a><a class="js_cancel" href="javascript:;">取消</a>'	
		},params.box,{
			content:'<div style="width:'+params.box.width+'px" class="form-box">'+content+'</div>'
		});

		var dia =new UI.dialog(options);
		dia.init();
		dia.show(); 
		
		dia.cont.find('.js_cancel').click(function(){
			dia.hide();
		})

		
		//查看的时候不需要表单验证及提交
		
		if(params.isView) return dia ;

		var form = dia.cont.find('form') ;

		dia.cont.find('.js_ok').click(function(){
			form.submit();
		}) 
		form.data('data',form.serialize());

		function showErr(msg){
			var box = form ,isDialog = options.isDialogError;
			if(isDialog){
				dia.hide();
				UI.alert(msg);		
			}else{
				var tip = box.find('.js_error_tip');
				if(tip.length==0){
					tip = $('<div class="error-tip js_error_tip"><b></b><a href="javascript:;" class="close">X</a></div>').prependTo(box);	
					tip.find('.close').click(function(){
						tip.hide();	
					})
				}
				tip.show().find('b').html(msg);	
			}
		}

		dia.cont.find('form').validate({
			rules:params.rules,
			messages:params.messages,
			submitHandler:function(form){
				var form = $(form) ;
				if(form.data('data')==form.serialize()) {//没有改变 不提交
					dia.hide();
					return ;
				}
				$.ajax({
					url:form.attr('action'),
					type:form.attr('method')?form.attr('method'):'get',
					data:form.serialize(),
					success:function(res){
						if(res.code == 0){
							if(params.cb){
								params.cb();		
							}else{
								location.reload();	
							}
						}else{
							showErr(res.msg||systemerror);
						}
					},
					complete:function(){
						//;
					},
					error:function(){
						showErr(systemerror);
					}
				})				
			}
		});
		return dia ;  
	}

	UI.reqDialog  = function(params){
		$.ajax({
			url:params.url,
			type:params.type||'get',
			dataType:'json',
			data:$.extend({},params.data),
			success:function(res){
				if(res.code==0){
					var template = params.template;
				  	Mustache.parse(template);   // optional, speeds up future uses
				  	var rendered = Mustache.render(template, res.data);
				  	var dialogParams = {
						box:$.extend(params.dialog,{
							isView:params.isView||false,
							content:rendered
						})
					}
					/**
					 * 是查看的时候 不需要规则
					 */
					if(!params.isView){
						$.extend(dialogParams,{
							rules:params.rules,
							messages:params.messages
						})
					}
					var dialog= UI.common(dialogParams);

					/**
					 * 自定义dialog按钮及回调                
					 */
					if(params.dialogCb){
						params.dialogCb(dialog,res);
					}

				}else{
					UI.alert(res.msg||systemerror);
				}
			},
			error:function(){
				UI.alert(systemerror);	
			}
		})
	}
	UI.view = function(ele,options){
		if($(ele).data('isReq')==true) return ;
		var url = options.url;
		var data = options.data||{};
		var method = options.type ||'get';
		$.ajax({
			url:url,
			data:data,
			//dataType:'json',
			success:function(res){
				if(res){
					var box = {
						title:'查看',
						width:830,
						btnContent:''
					}
					//$.extend(box,options.box,{content:res.html});
					$.extend(box,options.box,{content:res});
					UI.common({
						isView:true,
						box:box
					});
				}else{
					UI.alert(res.msg||systemerror);
				}
			},
			error:function(){
				UI.alert(systemerror);	
			},
			complete:function(){
				$(ele).data('isReq',false)
			}
		})		
	}
	UI.city = {
		init:function(ele,citys,callback){
			if($(ele).data('isReq')==true) return ;
			var url = '../static/getcitys.json';
			var that = this ; 
			this.citys = citys?citys.split(','):[];
			this.callback = callback || function(){} ;
			$.ajax({
				url:url,
				dataType:'json',
				success:function(res){
					if(res.code==0){
						var box = {
							title:'选择城市',
							width:1000,
							btnContent:'',
							noheader:true	
						}
						$.extend(box,{content:that.getHtml(res)});
						that.dia = UI.common({
							isView:true,
							box:box
						});
						that.bindEvent();
					}else{
						UI.alert(res.msg||systemerror);
					}
				},
				error:function(){
					UI.alert(systemerror);	
				},
				complete:function(){
					$(ele).data('isReq',false);
				}
			});	
		},
		bindEvent:function(){
			var that = this ;
			that.dia.cont.find('input[name=city]').change(function(){
				that.updateNum();		
			})
			that.dia.cont.find('.js_check_city').click(function(){
				that.dia.cont.find('input[name=city]').prop('checked',true);
				that.updateNum();	
			})
			that.dia.cont.find('.js_uncheck_city').click(function(){
				that.dia.cont.find('input[name=city]').prop('checked',false);
				that.updateNum(0);	
			})

			that.dia.cont.find('.js_uncheck_area').click(function(){
				$(this).closest('.city-item').find('input[name=city]').prop('checked',false);	
				that.updateNum();
			})
			that.dia.cont.find('.js_check_area').click(function(){
				$(this).closest('.city-item').find('input[name=city]').prop('checked',true);	
				that.updateNum();
			})
			that.dia.cont.find('.js_select_city_ok').click(function(){
				var keys = [] ,values= [];
				var checkeds = that.dia.cont.find('input[name=city]:checked');
				
				checkeds.each(function(){
					keys.push($(this).val());
					values.push($(this).attr('data-name'));
				});
				that.callback&&that.callback(keys.join(','),values.join(','));
				that.dia.remove();
			})
			that.dia.cont.find('.js_close').click(function(){
				that.dia.remove();
			})
		},
		updateNum:function(num){
			var n = num==0?0:this.dia.cont.find('input[name=city]:checked').length;
			this.dia.cont.find('.js_city_num').html('('+n+')');
		},
		getHtml:function(res){
			var header = '',body='',footer = '';
			header = ['<div class="dialog-con">',
					    '<div class="all-city-con">',
					        '<div class="city-check-title">',
					            '<h4>选择城市</h4>',
					            '<div class="city-num js_city_num">('+this.citys.length+')</div>',
					            '<div class="all-select">',
					                '<a href="javascript:;" class="js_check_city">全选</a>',
					                '<a href="javascript:;" class="js_uncheck_city">全不选</a>',
					                '<a href="javascript:;" class="js_close">关闭</a>',
					            '</div>',
					        '</div>',
					        '<div class="city-items">'].join('');
	      	footer = ['</div>',
					    '</div>',
					    '<div class="submit-btn city-store">',
					        '<a href="javascript:;" class="btn red-btn js_select_city_ok" >确定选择</a>',
					    '</div>',
					'</div>'].join('');
			for(var i in res.result){
				if(i=='海外区域') break ;
				body += ['<div class="city-item">',
			                '<dl>',
			                    '<dt>',
			                        '<b>'+i+'</b>',
			                        '<a href="javascript:;" class="js_check_area">全选</a>',
			                        '<a href="javascript:;" class="js_uncheck_area">全不选</a>',
			                    '</dt>'].join('')+this.getAreaHtml(res.result[i]) + '</dl></div>' ;
			}
			return header+body+footer ;
		},
		getAreaHtml:function(data){
			var html = '';
			for(var i in data){
				html += ['<dd class="city-detail">',
	                        '<p class="city-szm">'+i+'</p>',
	                        '<div class="city-list-num">'].join('')+this.getOneCity(data[i])+'</div></dd>';
			}
			return html ;    
		},
		getOneCity:function(data){
			var html = '';
			for(var i in data){
				var checked = '';
				for(var j = 0;j<this.citys.length ;j++){
					if(this.citys[j]==i) {
						checked = 'checked' ;		
					}
				} 
				html += '<label><input type="checkbox" name="city" '+checked+' data-name="'+data[i]+'" value="'+i+'">'+data[i]+'</label>';		           
			}
			return html ;  
		}
	}
})(UI)
$(function(){
	//UI.showMessage('操作成功');
	/*UI.confirm({
		content:'<div class="tip">确认删除吗</div>'
	},function(){
		alert('ok')
	})*/
	/*UI.common({
		box:{
			title:'添加2XXX',
			content:['<form action="save.json" method="get">',
						'<table>',
						     '<colgroup>',
						        '<col width="40">',
						        '<col width="*">',
						    '</colgroup>',
						    '<tr>',
						        '<td>名字：</td>',
						        '<td><input class="txt long-txt" name="name" placeholder="请填写" /></td>',
						    '</tr>',
						    '<tr>',
						        '<td>昵称：</td>',
						        '<td><input class="txt mid-txt" name="nick" placeholder="请填写" /></td>',
						    '</tr>',
						    '</table>',
					'</form>'].join(''),
			width:300		
		},
		rules:{name:'required',nick:'required'},
		messages:{name:'请填写名字',nick:'请填写昵称'}
	})	*/
	
})


