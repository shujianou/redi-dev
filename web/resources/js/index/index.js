$(function () {

    init();

    function init() {
        $.model.loadResources();
        initHostNames();
        initGroupNames();
        // select2初始化
        $('select').select2({
            minimumResultsForSearch: -1
        });
        servicePanel();
        xyData();
        dynamicPanel();
    }

    $('.logout').click(function () {
        $.security.logut();
    })


    /**
     * 初始化主机下拉框
     */
    function initHostNames() {
        $.ajax({
            url: API_PATH + '/monitor/host/nameList',
            type: 'post',
            async: false,
            success: function (res) {
                if (res.code == 200) {
                    var hosts = res.data.hosts;
                    var options = '';
                    for (var i = 0; i < hosts.length; i++) {
                        options += '<option value="' + hosts[i].id + '">' + hosts[i].name + '</option>'
                    }
                    $('#add-host-service').html(options);
                }
            }
        })
    }

    /**
     * 初始化集群下拉框
     */
    function initGroupNames() {
        $.ajax({
            url: API_PATH + '/monitor/group/selectIdAndGroupName',
            type: 'post',
            async: false,
            success: function (res) {
                if (res.code == 200) {
                    var groups = res.data.groups;
                    var options = '';
                    for (var i = 0; i < groups.length; i++) {
                        options += '<option value="' + groups[i].id + '">' + groups[i].groupName + '</option>'
                    }
                    $('#add-group-service').html(options);
                }
            }
        })
    }


    $('#add-save-btn-service').click(function () {
        var json = $('#add-form-service').serializeObject();
        if (!json.name) {
            $.message({
                type: 'warning',
                message: '请输入服务名'
            })
            return false;
        }
        if (!$.check.email(json.noticeEmail)) {
            $.message({
                type: 'warning',
                message: '请正确填写通知邮箱'
            })
            return false;
        }
        if (!$.check.ip(json.appHost)) {
            $.message({
                type: 'warning',
                message: '请输入正确的服务地址'
            })
            return false;
        }
        if (!json.port) {
            $.message({
                type: 'warning',
                message: '请输入服务端口'
            })
            return false;
        }
        $.ajax({
            url: API_PATH + '/monitor/app/save',
            type: 'post',
            data: json,
            success: function (res) {
                if (res.code == 200) {
                    $.message('添加成功');
                    if (!$('#continue-add-chk').is(':checked')) {
                        servicePanel();
                        $('#serviceDialog').modal('hide');
                    }
                } else {
                    $.message({
                        type: 'error',
                        message: res.msg
                    })
                }
            }
        })
    })

    $('#add-save-btn-group').click(function () {
        var json = $('#add-form-group').serializeObject();
        if (!json.groupName) {
            $.message({
                type: 'warning',
                message: '请输入集群名'
            })
            return false;
        }
        if (!json.groupManager) {
            $.message({
                type: 'warning',
                message: '请输入集群负责人'
            })
            return false;
        }
        if (!$.check.email(json.noticeEmail)) {
            $.message({
                type: 'warning',
                message: '请输入正确的邮箱地址'
            })
            return false;
        }
        $.ajax({
            url: API_PATH + '/monitor/group/save',
            type: 'post',
            data: json,
            success: function (res) {
                if (res.code == 200) {
                    $.message('添加成功');
                    if (!$('#continue-add-chk').is(':checked')) {
                        servicePanel();
                        $('#groupDialog').modal('hide');
                    }
                } else {
                    $.message({
                        type: 'error',
                        message: res.msg
                    })
                }
            }
        })
    })


    $('#add-save-btn-host').click(function () {
        var json = $('#add-form-host').serializeObject();
        debugger
        if (!json.name) {
            $.message({
                type: 'warning',
                message: '请输入主机名称'
            })
            return false;
        }
        if (!json.host) {
            $.message({
                type: 'warning',
                message: '请输入主机地址'
            })
            return false;
        }
        if (!json.username) {
            $.message({
                type: 'warning',
                message: '请输入登录用户名'
            })
            return false;
        }
        if (!json.password) {
            $.message({
                type: 'warning',
                message: '请输入登录密码'
            })
            return false;
        }
        $.ajax({
            url: API_PATH + '/monitor/host/save',
            type: 'post',
            data: json,
            success: function (res) {
                if (res.code == 200) {
                    $.message('添加成功');
                    if (!$('#continue-add-chk').is(':checked')) {
                        servicePanel();
                        $('#hostDialog').modal('hide');
                    }
                } else {
                    $.message({
                        type: 'error',
                        message: res.msg
                    })
                }
            }
        })
    })


    $('#add-btn-service').click(function () {
        document.getElementById("add-form-service").reset();
        $('#serviceDialog').modal('show');
    })

    $('#add-btn-group').click(function () {
        document.getElementById("add-form-group").reset();
        $('#groupDialog').modal('show');
    })

    $('#add-btn-host').click(function () {
        document.getElementById("add-form-host").reset();
        $('#hostDialog').modal('show');
    })

    /**
     * 获取服务面版数据
     */
    function servicePanel() {
        $.ajax({
            url: API_PATH + '/main/servicePanel',
            type: 'post',
            success: function (res) {
                if (res.code == 200) {
                    var data = res.data;
                    $('#system-info').html(data.serverInfo.serverName + '(' + data.serverInfo.serverArch + ')');
                    $('#service-num').html(data.serviceNum);
                    $('#group-num').html(data.groupNum);
                    $('#host-num').html(data.hostNum);
                    $('#database-num-num').html(0);
                }
            }
        })
    }

    /**
     * 获取JOB监测数据
     */
    function xyData() {
        $.ajax({
            url: API_PATH + '/log/countByCTime',
            type: 'post',
            success: function (res) {
                // debugger
                if (res.code === 200) {
                    var myChart = echarts.init(document.getElementById('line'));
                    // 指定图表的配置项和数据
                    var option = {
                        xAxis: {
                            type: 'category',
                            boundaryGap: false,
                            data: res.data.xData
                        },
                        yAxis: {
                            type: 'value'
                        },
                        series: [{
                            data: res.data.yData,
                            type: 'line',
                            areaStyle: {}
                        }]
                    };
                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);
                }
            }
        })
    }

    $("#fuwu_list ul").on("mouseover","li",function(){
        var left = $(this).offset().left;
        var top = $(this).offset().top + 25;
        $(this).children(".model_Fw").css("left",left+"px");
        $(this).children(".model_Fw").css("top",top+"px");
        $(this).children(".model_Fw").toggle();
    });
    $("#fuwu_list ul").on("mouseout","li",function(){
        $(this).children(".model_Fw").toggle();
    })



















    /**
     * 获取面版动态数据
     */
	PercentPie.prototype.init = function () {
		var _that = this;
		var option = {
			backgroundColor: _that.backgroundColor,
			color: _that.color,
			title: {
				text: _that.title,
				top: '3%',
				left: '1%',
				textStyle: {
					color: '#333',
					fontStyle: 'normal',
					fontWeight: 'normal',
					fontFamily: 'sans-serif',
					fontSize: 16,
				}
			},
			series: [{
				name: '来源',
				type: 'pie',
				radius: ['60%', '75%'],
				avoidLabelOverlap: false,
				hoverAnimation: false,
				label: {
					normal: {
						show: false,
						position: 'center',
						textStyle: {
							fontSize: _that.fontSize,
							fontWeight: 'bold'
						},
						formatter: '{b}\n{c}%'
					}
				},
				data: [{
					value: _that.value,
					name: _that.name,
					label: {
						normal: {
							show: true
						}
					}
				},
					{
						value: 100 - _that.value,
						name: ''
					}
				]
			}]
		};

		echarts.init(_that.domEle).setOption(option);
	};
	function Create_Pie(){
		this.option2 = {
					value: 2,
					name: '',
					backgroundColor: null,
					color: ['#29A176', '#e6e9ee'],
					fontSize: 16,
					domEle: document.getElementById("circular_S")
				};
// 		percentPie2 = new PercentPie(option2);
// 		percentPie2.init();
	
		this.option3 = {
				value: 3,
				name: '',
				backgroundColor: null,
				color: ['#29A176', '#e6e9ee'],
				fontSize: 16,
				domEle: document.getElementById("circular_T")
			};
// 		percentPie3 = new PercentPie(option3);
// 		percentPie3.init();
	
		this.option4 = {
				value: 4,
				name: '',
				backgroundColor: null,
				color: ['#29A176', '#e6e9ee'],
				fontSize: 16,
				domEle: document.getElementById("circular_D")
			};
// 		percentPie4 = new PercentPie(option4);
// 		percentPie4.init();

	}
	var tet = new Create_Pie();
	dynamicPanel(tet);

	function fuwuBox(info){
		if(info.runStatus == 1){
			var $mainServerState = $("<div class='main-serverState'></div>");
			var $title = $('<div class="main-serverState-title" data-id="'+info.id+'"><span class="fuwuqi_name">' + info.name + '</span><div class="server_button">添加服务</div></div>');
			var $stateList = $('<div id="state_list"></div>');
			var $CPU = $('<div class="col-md-2 col-xs-6 circular"><div class="main-serverState-modularTitle">CPU使用率</div><div id="circular_S+' + info.id + '" style="width: 100px;height: 100px;margin: 0 auto;background-color: #29A176;"></div><div class="main-serverState-modularntroduce"></div></div>')
			var $MEM = $('<div class="col-md-2 col-xs-6 circular"><div class="main-serverState-modularTitle">内存使用率</div><div id="circular_T+' + info.id + '" style="width: 100px;height: 100px;margin: 0 auto;background-color: #29A176;"></div><div class="main-serverState-modularntroduce"><span id="mem-used' + info.id + '">1</span>(MB)</div></div>')
			var $DISK = $('<div class="col-md-2 col-xs-6 circular"><div class="main-serverState-modularTitle">硬盘</div><div id="circular_D+' + info.id + '" style="width: 100px;height: 100px;margin: 0 auto;background-color: #29A176;"></div><div class="main-serverState-modularntroduce"><span id="disk-used' + info.id + '">1</span></div></div>')
			var $clearBox = $('<div class="clearfix"></div>');
			var $fuwuList = $('<div id="fuwu_list" class="col-md-5 col-xs-6 circular"></div>');
			var $ul = $('<ul></ul>');
			var list = info.services;
			if(list != null ){
				for (var i = list.length - 1; i >= 0; i--) {
					if (list[i].runStatus == 1) {
						var $li = $("<li class='runIcon' data-id=" + list[i].id + "><span></span><div class='model_Fw'></div>" + list[i].name + "</li>");
					}
					else {
						var $li = $("<li class='stopIcon' data-id=" + list[i].id + "><span></span><div class='model_Fw'></div>" + list[i].name + "</li>");
					}
					$ul.prepend($li);
				}
			}
			$fuwuList.append($ul);
			$stateList.append($CPU);
			$stateList.append($MEM);
			$stateList.append($DISK);
			$stateList.append($fuwuList);
			$stateList.append($clearBox);
			$mainServerState.append($title);
			$mainServerState.append($stateList);
			$('.main').append($mainServerState);
			console.log(info.services);
		}
		else{
			var $mainServerState = $("<div class='main-serverState'></div>");
			var $title = $('<div class="main-serverState-title"><span class="fuwuqi_name">' + info.name + ':未运行</span><div class="server_button">添加服务</div></div>');
			$mainServerState.append($title);
			$('.main').append($mainServerState);
		}
	}
	function ZJ_list(){
		$.ajax({
			url: API_PATH + '/main/dynamicPanel',
			type: 'post',
			success: function (res) {
				var data = res.data;
				if (res.code == 200) {
					$(".main-serverState").remove();
					for(var i = 0;i< data.dynamicHostInfo.length;i++ ){
						//CPU
						// console.log(data.dynamicHostInfo[i]);
						fuwuBox(data.dynamicHostInfo[i]);
					}
				}
			},
		})
	}
	ZJ_list();
    function dynamicPanel(tet) {
		// console.log(tet);
        $.ajax({
            url: API_PATH + '/main/dynamicPanel',
            type: 'post',
            success: function (res) {
				var data = res.data;
				// console.log(data.dynamicHostInfo[0]);
                if (res.code == 200) {
					for(var i = 0;i< data.dynamicHostInfo.length;i++ ){
						//CPU
						// console.log(data.dynamicHostInfo[i]);
// 						fuwuBox(data.dynamicHostInfo[i].info);
						// console.log(data.dynamicHostInfo[i].runStatus == 1)
						if(data.dynamicHostInfo[i].runStatus == 1){
							//CPU
							// console.log(data.dynamicHostInfo[i]);
							tet.option2.value = data.dynamicHostInfo[i].info.cpuRate;
							var option2_dom = "circular_S+" + data.dynamicHostInfo[i].id;
							tet.option2.domEle = document.getElementById(option2_dom);
							// $('#cpu-num+' + data.dynamicHostInfo[i].id).html(data.cpuNum);
							percentPie2 = new PercentPie(tet.option2);
							percentPie2.init();

							//内存
							tet.option3.value = data.dynamicHostInfo[i].info.memRate;
							var option3_dom = "circular_T+" + data.dynamicHostInfo[i].id;
							tet.option3.domEle = document.getElementById(option3_dom);
							$("#mem-used" + data.dynamicHostInfo[i].id).html(data.dynamicHostInfo[i].info.memUsed + '/' + data.dynamicHostInfo[i].info.memTotal);
							percentPie3 = new PercentPie(tet.option3);
							percentPie3.init();

							//硬盘
							tet.option4.value = data.dynamicHostInfo[i].info.diskRate;
							var option4_dom = "circular_D+" + data.dynamicHostInfo[i].id;
							tet.option4.domEle = document.getElementById(option4_dom);
							$("#disk-used" + data.dynamicHostInfo[i].id).html(data.dynamicHostInfo[i].info.diskUsed + 'G/' + data.dynamicHostInfo[i].info.diskTotal + 'G');
							percentPie4 = new PercentPie(tet.option4);
							percentPie4.init();
						}
						
					}

                    function getServiceInfo(serviceInfo) {
                        if (serviceInfo) {
                            return serviceInfo.info;
                        } else {
                            return '';
                        }
                    }
                }
            }
        })
    }
	
	/**
	 * 点击提交 获取checkbox的id值	
	**/
	$(".zhuji_button").click(function(){
		checkboxList();
		$("#zhuji-list-box").show();
	})
	$(".main").on('click',".server_button",function(){
		var id = $(this).parent().data("id");
		NowID = id;
		checkbox_fuwuList(id);
		$("#service-list-box").show();
	})
	$(".zhuji_close").click(function(){
		$("#zhuji-list-box").hide();
	})
	$(".service_close").click(function(){
		$("#service-list-box").hide();
	})
	var serviceHostID;
	var NowID;
	function checkbox_fuwuList(id){
		$.ajax({
			url: API_PATH + '/main/dynamicPanel',
			type: 'post',
			success: function (res) {
				var data = res.data;
				console.log(data);
				var list = data.serviceNameList;
				console.log(id);
				var RunStatus = data.dynamicHostInfo;
				var OBJ;
				for(var n = 0; n < RunStatus.length;n++){
					if(RunStatus[n].id == id){
						serviceHostID = RunStatus[n].hostPanelId;
						OBJ = RunStatus[n].services;
					}
				}
				// debugger
				$("#service-list-box .box-list .opt").empty();
				for(var i = list.length - 1; i >= 0; i--){
					if(OBJ == null){
						var $checkbox = $('<input class="magic-checkbox" type="checkbox" name="layout" id="' + list[i].id + '"><label for="' + list[i].id + '">' + list[i].name + '</label>');
					}
					else{
						for(var j = OBJ.length - 1; j >= 0; j--){
							if(list[i].hostId == id && list[i].id == OBJ[j].id){
								var $checkbox = $('<input class="magic-checkbox" type="checkbox" checked name="layout" id="' + list[i].id + '"><label for="' + list[i].id + '">' + list[i].name + '</label>');
								break;
							}
							if(list[i].hostId == id && list[i].id != OBJ[j].id){
								var $checkbox = $('<input class="magic-checkbox" type="checkbox" name="layout" id="' + list[i].id + '"><label for="' + list[i].id + '">' + list[i].name + '</label>');
							}
						}
					}
					
					$("#service-list-box .box-list .opt").append($checkbox);
				}
			},
		})
	}
	function checkboxList(){
		$.ajax({
			url: API_PATH + '/main/dynamicPanel',
			type: 'post',
			success: function (res) {
				var data = res.data;
				console.log(data);
				var list = data.hostIdNameList;
				var RunStatus = data.dynamicHostInfo;
				$(".box-list .opt").empty();
				for(var i = list.length - 1; i >= 0; i--){
					console.log(i);
					if(RunStatus.length == 0){
						var $checkbox = $('<input class="magic-checkbox" type="checkbox" name="layout" id="' + list[i].id + '"><label for="' + list[i].id + '">' + list[i].name + '</label>');
					}
					else{
						for(var j = RunStatus.length - 1; j>=0;j--){
							console.log(j);
							if(list[i].id == RunStatus[j].id){
								var $checkbox = $('<input class="magic-checkbox" type="checkbox" checked name="layout" id="' + list[i].id + '"><label for="' + list[i].id + '">' + list[i].name + '</label>');
								break;
							}
							if(j == 0 && list[i].id != RunStatus[j].id){
								var $checkbox = $('<input class="magic-checkbox" type="checkbox" name="layout" id="' + list[i].id + '"><label for="' + list[i].id + '">' + list[i].name + '</label>');
							}
						}
					}
					
					// var $checkbox = $('<input class="magic-checkbox" type="checkbox" name="layout" id="' + list[i].id + '"><label for="' + list[i].id + '">' + list[i].name + '</label>');
					$(".box-list .opt").append($checkbox);
				}
			},
		})
	}
	$(".service-footer").click(function(){
		var list = [];
		$.each($("#service-list-box .box-list .opt").children("input[type=checkbox]:checked"),function(){
				list.push($(this).attr("id"));
			});
			list = JSON.stringify( list );
			console.log(list);
			$.ajax({
				url: API_PATH + '/main/addHomeBindInfo/0/' + serviceHostID,
				type: 'post',
				data: {
					"ids":list,
				},
				datatype:'json',
				success: function (res) {
					console.log(res);
				},
			})
		
// 		var $fuwuList = $('<div id="fuwu_list" class="col-md-5 col-xs-6 circular"></div>');
// 		var $ul = $('<ul></ul>');
// 		for (var i = list.length - 1; i >= 0; i--) {
// 			if (list[i].runStatus == 1) {
// 				var $li = $("<li class='runIcon' data-id=" + list[i].id + "><span></span><div class='model_Fw'></div>" + list[i].name + "</li>");
// 			}
// 			else {
// 				var $li = $("<li class='stopIcon' data-id=" + list[i].id + "><span></span><div class='model_Fw'></div>" + list[i].name + "</li>");
// 			}
// 			$ul.prepend($li);
// 		}
// 		
// 		$(".main-serverState" )
// 		
		ZJ_list();
		$("#service-list-box").hide();
	})
	$(".zhuji-footer").click(function(){
		var list = [];
		$.each($("#zhuji-list-box .box-list .opt").children("input[type=checkbox]:checked"),function(){
                list.push($(this).attr("id"));
            });
			list = JSON.stringify( list );
			console.log(list);
			$.ajax({
				url: API_PATH + '/main/addHomeBindInfo/1/123',
				type: 'post',
				data: {
					"ids":list,
				},
				datatype:'json',
				success: function (res) {
					console.log(res);
				},
			})
		ZJ_list();
		$("#zhuji-list-box").hide();
	})
	
	
    function PercentPie(option) {
        this.backgroundColor = option.backgroundColor || '#fff';
        this.color = option.color || ['#38a8da', '#d4effa'];
        this.fontSize = option.fontSize || 12;
        this.domEle = option.domEle;
        this.value = option.value;
        this.name = option.name;
        this.title = option.title;
    }
























    /* var option1 = {
             value: 1,//百分比,必填
             name: '',
             backgroundColor: null,
             color: ['#29A176', '#e6e9ee'],
             fontSize: 16,
             domEle: document.getElementById("circular_F")
         },
         percentPie1 = new PercentPie(option1);
     percentPie1.init();
 */
	
})