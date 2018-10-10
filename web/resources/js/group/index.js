$(function () {
    var $table = $('#table');

    var groupNames = [];


    init();

    function init() {
        initGroupNames();
        // select2初始化
        $('select').select2({
            minimumResultsForSearch: -1
        });
        initTable();
    }

    $(document).on('focus', 'input[type="text"]', function () {
        $(this).parent().find('label').addClass('active');
    }).on('blur', 'input[type="text"]', function () {
        if ($(this).val() == '') {
            $(this).parent().find('label').removeClass('active');
        }
    });
    var editor;

    function initGroupNames(){
        $.ajax({
            url:API_PATH+'/monitor/group/selectIdAndGroupName?type=0',
            type:'post',
            async:false,
            success:function (res) {
                debugger;
                if (res.code==200){
                    var options;
                    groupNames=res.data;
                }
            }
        })
    }

    /**
     * 初始化表格
     */
    function initTable(pid) {
        var url = API_PATH + '/monitor/group/query';

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
                    Q_KEY: 'group_name,group_manager,notice_email'
                };
                if (pid) {
                    querys = {
                        P_NO: params.pageNumber,
                        P_SIZE: params.pageSize,
                        P_ORDER: params.sortOrder,
                        P_SORT: params.sortName,
                        Q_SEARCH: params.searchText,
                        Q_KEY: 'group_name,group_manager,notice_email',
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
                {field: 'groupName', title: '集群名称', sortable: true, halign: 'center'},
                {field: 'groupManager', title: '集群负责人', sortable: true, halign: 'center'},
                {field: 'noticeEmail', title: '联系邮箱', sortable: true, halign: 'center'},
                {
                    field: 'status', title: '状态', sortable: true, halign: 'center',
                    formatter: function (val, row, index) {
                        if (val == 0) {
                            return '禁用';
                        } else if (val == 1) {
                            return '启用';
                        }
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

    $('#add-save-btn').click(function () {
        var json = $('#add-form').serializeObject();
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
                    $table.bootstrapTable('refresh');
                    if (!$('#continue-add-chk').is(':checked')){
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

    window.actionEvents = {
        'click .code': function (e, value, row, index) {
            $('#shell-id').val(row.id);
            if (row.command) {
                var command = row.command;
                editor.setValue(command);
            } else {
                editor.setValue('');
            }
            $('#shell-code-div').slideDown("slow");
        },
        'click .edit': function (e, value, row, index) {
            $('#createDialog .modal-body label').attr('class', 'active');
            $('.modal-title').html('编辑集群');
            $('#add-id').val(row.id);
            $('#add-group-name').val(row.groupName);
            $('#add-group-manager').val(row.groupManager);
            $('#add-notice-email').val(row.noticeEmail);
            debugger
            $('#add-status').val(row.status);
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
                                url: API_PATH + '/monitor/group/delete',
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
        $('.modal-title').html('新增服集群');
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
                            debugger;
                            var ids = [];
                            for (var i in rows) {
                                ids.push(rows[i].id);
                            }
                            $.ajax({
                                contentType: "application/json",
                                url: API_PATH + '/monitor/group/deleteBatchIds',
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
