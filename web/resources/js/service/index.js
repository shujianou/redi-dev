$(function () {
    var $table = $('#table');

    var groupNames = [];
    var hostNames = [];

    var $appNameSelect;


    init();

    function init() {
        initAppNames();
        initHostNames();
        initGroupNames();

        $('#add-status').select2({
            minimumResultsForSearch: -1
        });

        initTable();
        initShellCode();
    }

    $(document).on('focus', 'input[type="text"]', function () {
        $(this).parent().find('label').addClass('active');
    }).on('blur', 'input[type="text"]', function () {
        if ($(this).val() == '') {
            $(this).parent().find('label').removeClass('active');
        }
    });
    var editor;

    function initHostNames() {
        $.ajax({
            url: API_PATH + '/monitor/host/nameList',
            type: 'post',
            async: false,
            success: function (res) {
                if (res.code == 200) {
                    var hosts = res.data.hosts;
                    hostNames = res.data.names;
                    var options = '';
                    for (var i = 0; i < hosts.length; i++) {
                        options += '<option value="' + hosts[i].id + '">所属主机:[' + hosts[i].name + ']</option>'
                    }
                    $('#add-host').html(options);
                    $('#add-host').select2({
                        minimumResultsForSearch: -1
                    });
                }
            }
        })
    }

    function initAppNames() {
        $.ajax({
            url: API_PATH + '/monitor/app/nameList',
            type: 'post',
            async: false,
            success: function (res) {
                if (res.code == 200) {
                    var apps = res.data;
                    var options = '<option value="-1">请选择依赖服务(不选择默认不检查依赖)</option>';
                    for (var i = 0; i < apps.length; i++) {
                        options += '<option value="' + apps[i].id + '">依赖于:[' + apps[i].name + ']</option>'
                    }
                    $('#add-rely-app').html(options);

                    $appNameSelect=$('#add-rely-app').select2({
                        minimumResultsForSearch: -1,
                        multiple : true //多选
                    });
                }
            }
        })
    }

    /**
     * 初始化集群下拉框
     */
    function initGroupNames() {
        debugger
        $.ajax({
            url: API_PATH + '/monitor/group/selectIdAndGroupName',
            type: 'post',
            async: false,
            success: function (res) {
                if (res.code == 200) {
                    var groups = res.data.groups;
                    groupNames = res.data.names;
                    var options = '';
                    for (var i = 0; i < groups.length; i++) {
                        options += '<option value="' + groups[i].id + '">所属集群:[' + groups[i].groupName + ']</option>'
                    }
                    $('#add-group').html(options);
                    $('#add-group').select2({
                        minimumResultsForSearch: -1
                    });
                }
            }
        })
    }

    /**
     * 初始化shell编辑器
     */
    function initShellCode() {
        editor = CodeMirror.fromTextArea(document.getElementById("shell-code"), {
            mode: "text/shell",    //实现groovy代码高亮
            //mode: "text/x-java", //实现Java代码高亮
            lineNumbers: true,	//显示行号
            theme: "dracula",	//设置主题
            lineWrapping: true,	//代码折叠
            foldGutter: true,
            gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter"],
            matchBrackets: true,	//括号匹配
            //readOnly: true,        //只读
            extraKeys: {
                'Ctrl-S': function () {
                    saveShell();
                }
            }
        });
        //如果store中有缓存则取出来
        var code = store.get('shellCode');
        if (code) {
            editor.setValue(code);
        }
    }

    /**
     * 初始化表格
     */
    function initTable(pid) {
        var url = API_PATH + '/monitor/app/query';

        $table.bootstrapTable({
            url: url,
            responseHandler: function (res) {
                return {
                    total: res.records,
                    rows: res.rows
                }
            },
            queryParams: function (params) {
                var querys = {
                    P_NO: params.pageNumber,
                    P_SIZE: params.pageSize,
                    P_ORDER: params.sortOrder,
                    P_SORT: params.sortName,
                    Q_SEARCH: params.searchText,
                    Q_KEY: 'name,app_host,retry_command'
                };
                if (pid) {
                    querys = {
                        P_NO: params.pageNumber,
                        P_SIZE: params.pageSize,
                        P_ORDER: params.sortOrder,
                        P_SORT: params.sortName,
                        Q_SEARCH: params.searchText,
                        Q_KEY: 'name,app_host,retry_command',
                        Q_COLUMN: 'AND_parent_id_EQ,OR_id_EQ',
                        Q_VALUE: pid + ',' + pid
                    }
                }
                return querys;
            },
            pageSize: 8,
            pageNumber: 1,
            queryParamsType: '',
            height: getHeight(),
            pageList: [5, 10, 25, 50, 100],
            striped: true,
            search: true,
            searchOnEnterKey: true,
            showRefresh: true,
            showToggle: true,
            showColumns: true,
            minimumCountColumns: 2,
            showPaginationSwitch: true,
            clickToSelect: true,
            detailView: false,
            detailFormatter: 'detailFormatter',
            pagination: true,
            paginationLoop: false,
            classes: 'table table-hover table-no-bordered',
            sidePagination: 'server',
            silentSort: false,
            smartDisplay: false,
            idField: 'id',
            sortName: 'id',
            sortOrder: 'asc',
            escape: true,
            maintainSelected: true,
            toolbar: '#toolbar',
            columns: [
                {field: 'state', checkbox: true},
                {field: 'id', title: '编号', sortable: true, halign: 'center'},
                {field: 'name', title: '服务名称', sortable: true, halign: 'center'},
                {field: 'logicalName', title: '逻辑名', sortable: true, halign: 'center'},
                {field: 'appHost', title: '服务地址', sortable: true, halign: 'center'},
                {field: 'port', title: '端口', sortable: true, halign: 'center'},
                {
                    field: 'groupId', title: '所属集群', sortable: true, halign: 'center',
                    formatter: function (val, row, index) {
                        return groupNames[val];
                    }
                },
                {
                    field: 'hostId', title: '所在主机', sortable: true, halign: 'center',
                    formatter: function (val, row, index) {
                        
                        return hostNames[val];
                    }
                },
                {field: 'retryCommand', title: '重启Shell', sortable: true, halign: 'center', visible: false},
                {field: 'retryConnection', title: '重连次数', sortable: true, halign: 'center'},
                {
                    field: 'runStatus', title: '运行状态', sortable: true, halign: 'center',
                    formatter: function (val, row, index) {
                        
                        if (val === 0) {
                            return '<img src="/resources/images/stop.png" height="30" width="30"/>';
                        } else if (val === 1) {
                            return '<img src="/resources/images/running.png" height="30" width="30"/>';
                        }
                    }
                },
                {
                    field: 'status', title: '状态', sortable: true, halign: 'center',
                    formatter: function (val, row, index) {
                        if (val === 0) {
                            return '禁用';
                        } else if (val === 1) {
                            return '启用';
                        }
                    }
                },
                {field: 'checkType', title: '检测类型', sortable: true, halign: 'center'},
                {field: 'sort', title: '排序', sortable: true, halign: 'center'},
                {field: 'noticeEmail', title: '通知邮箱', sortable: true, halign: 'center'},
                {field: 'creator', title: '创建人', sortable: true, halign: 'center'},
                {
                    field: 'createTime', title: '创建时间', sortable: true, halign: 'center',
                    formatter: function (val, row, index) {
                        return dateFormatter(val);
                    }
                },
                {
                    field: 'updateTime', title: '更新时间', sortable: true, halign: 'center',
                    formatter: function (val, row, index) {
                        return dateFormatter(val);
                    }
                },
                {
                    field: 'action',
                    title: '操作',
                    halign: 'center',
                    align: 'center',
                    formatter: 'actionFormatter',
                    events: 'actionEvents',
                    clickToSelect: false
                }
            ]
        }).on('all.bs.table', function (e, name, args) {
            $('[data-toggle="tooltip"]').tooltip();
            $('[data-toggle="popover"]').popover();
        });
    }

    /**
     * 保存shell
     */
    function saveShell() {
        var id = $('#shell-id').val();
        if (!id) {
            $.message({
                'type': 'warning',
                'message': '请选择需要修改的服务信息'
            })
            return false;
        }
        $.ajax({
            url: API_PATH + '/monitor/app/save',
            type: 'post',
            data: {
                id: id,
                retryCommand: editor.getValue()
            },
            success: function (res) {
                if (res.code == 200) {
                    $.message('保存成功');
                    $table.bootstrapTable('refresh');
                } else {
                    $.message({
                        'type': 'error',
                        'message': res.code + ':' + res.msg
                    })
                }
            }
        })
    }

    $('#add-save-btn').click(function () {
        var json = $('#add-form').serializeObject();
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
        debugger
        //json.relyId.remove('-1');
        if (json.relyId.indexOf(',')>-1) {
            json.relyId = json.relyId.join(',');
        }
        $.ajax({
            url: API_PATH + '/monitor/app/save',
            type: 'post',
            data: json,
            success: function (res) {
                if (res.code == 200) {
                    $.message('添加成功');
                    $table.bootstrapTable('refresh');
                    if (!$('#continue-add-chk').is(':checked')) {
                        $('#createDialog').modal('hide');
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
    /**
     * 监听shell编辑器space按键内容入store
     */
    $('#shell-code-div').keypress(function (e) {
        if (32 == e.keyCode) {
            store.set('shellCode', editor.getValue());
        }
    })

    /**
     * 立即执行shell
     */
    $('#exec-shell-btn').click(function () {
        var id = $('#shell-id').val();
        if (!id) {
            $.message({
                'type': 'warning',
                'message': '请选择需要执行的服务'
            })
            return false;
        }
        $.ajax({
            url: API_PATH + '/monitor/app/execShell',
            type: 'post',
            data: {
                appId: id,
                retryCommand: editor.getValue()
            },
            success: function (res) {
                if (res.code == 200) {
                    $.message(res.msg);
                } else {
                    $.message({
                        'type': 'error',
                        'message': res.code + ':' + res.msg
                    })
                }
            }
        })
    })


    $('#shell-code-click').click(function () {
        $('#shell-code-div').slideToggle("slow");
    })
    window.actionEvents = {
        'click .code': function (e, value, row, index) {
            $('#shell-id').val(row.id);
            if (row.retryCommand) {
                var command = row.retryCommand;
                editor.setValue(command);
            } else {
                editor.setValue('');
            }
            $('#shell-code-div').slideDown("slow");
        },
        'click .edit': function (e, value, row, index) {
            $('#createDialog .modal-body label').attr('class', 'active');
            $('.modal-title').html('编辑服务');
            $('#add-name').val(row.name);
            $('#add-email').val(row.noticeEmail);
            $('#add-ip').val(row.appHost);
            $('#add-port').val(row.port);
            $('#add-retry-connection').val(row.retryConnection);
            $('#add-shell').val(row.retryCommand);
            $('#add-shell').hide();
            $('#add-id').val(row.id);
            $('#add-sort').val(row.sort);
            $('#add-status').val(row.status);
            $('#add-group').val(row.groupId);
            $('#add-host').val(row.hostId);
            debugger
            if (row.relyId){
                $appNameSelect.val(row.relyId.split(',')).trigger('change');
            }
            $('select').select2({
                minimumResultsForSearch: -1
            });

            $('#createDialog').modal('show');
        },
        'click .remove': function (e, value, row, index) {
            $.confirm({
                type: 'red',
                animationSpeed: 300,
                title: false,
                content: '确认删除该服务吗？',
                buttons: {
                    confirm: {
                        text: '确认',
                        btnClass: 'waves-effect waves-button',
                        action: function () {
                            var id = row.id;
                            $.ajax({
                                url: API_PATH + '/monitor/app/delete',
                                type: 'post',
                                data: {
                                    id: id
                                },
                                success: function (res) {
                                    if (res.code == 200) {
                                        $.message('操作成功');
                                        $table.bootstrapTable('refresh');
                                    } else {
                                        $.message({
                                            type: 'error',
                                            message: res.msg
                                        })
                                    }
                                }
                            })
                        }
                    },
                    cancel: {
                        text: '取消',
                        btnClass: 'waves-effect waves-button'
                    }
                }
            });

        }
    };


    $('#add-btn').click(function () {
        $('.modal-title').html('新增服务');
        document.getElementById("add-form").reset();
        $('#add-shell').show();
        $('#add-id').val('');
        $('#createDialog').modal('show');
    })

    $('#delete-batch-btn').click(function () {
        var rows = $table.bootstrapTable('getSelections');
        if (rows.length == 0) {
            $.confirm({
                title: false,
                content: '请至少选择一条记录！',
                autoClose: 'cancel|3000',
                backgroundDismiss: true,
                buttons: {
                    cancel: {
                        text: '取消',
                        btnClass: 'waves-effect waves-button'
                    }
                }
            });
        } else {
            $.confirm({
                type: 'red',
                animationSpeed: 300,
                title: false,
                content: '确认删除该服务吗？',
                buttons: {
                    confirm: {
                        text: '确认',
                        btnClass: 'waves-effect waves-button',
                        action: function () {
                            ;
                            var ids = [];
                            for (var i in rows) {
                                ids.push(rows[i].id);
                            }
                            $.ajax({
                                contentType: "application/json",
                                url: API_PATH + '/monitor/app/deleteBatchIds',
                                type: 'post',
                                //traditional: true,
                                data: JSON.stringify(ids),
                                success: function (res) {
                                    if (res.code == 200) {
                                        $.message('操作成功');
                                        $table.bootstrapTable('refresh');
                                    } else {
                                        $.message({
                                            type: 'error',
                                            message: res.msg
                                        })
                                    }
                                }
                            })
                        }
                    },
                    cancel: {
                        text: '取消',
                        btnClass: 'waves-effect waves-button'
                    }
                }
            });
        }
    })
});

/**
 * 时间格式化
 */
function dateFormatter(cellval) {
    var dateVal = cellval + "";
    if (cellval != null) {
        var date = new Date(parseInt(dateVal.replace("/Date(", "").replace(")/", ""), 10));
        var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
        var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();

        var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
        var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
        var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();

        return date.getFullYear() + "-" + month + "-" + currentDate + " " + hours + ":" + minutes + ":" + seconds;
    }
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
function actionFormatter(value, row, index) {
    return [
        '<a class="code" href="javascript:void(0)" data-toggle="tooltip" title="编写shell"><i class="glyphicon glyphicon-wrench"></i></a>　',
        '<a class="edit ml10" href="javascript:void(0)" data-toggle="tooltip" title="编辑"><i class="glyphicon glyphicon-edit"></i></a>　',
        '<a class="remove ml10" href="javascript:void(0)" data-toggle="tooltip" title="删除"><i class="glyphicon glyphicon-remove"></i></a>'
    ].join('');
}


function detailFormatter(index, row) {
    var html = [];
    $.each(row, function (key, value) {
        html.push('<p><b>' + key + ':</b> ' + value + '</p>');
    });
    return html.join('');
}
