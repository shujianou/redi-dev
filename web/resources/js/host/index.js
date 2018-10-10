$(function () {
    var $table = $('#table');
    init();

    function init() {
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

    /**
     * 初始化表格
     */
    function initTable(pid) {
        var url = API_PATH + '/monitor/host/query';

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
                    Q_KEY: 'host,username,creator,modifier'
                };
                if (pid) {
                    querys = {
                        P_NO: params.pageNumber,
                        P_SIZE: params.pageSize,
                        P_ORDER: params.sortOrder,
                        P_SORT: params.sortName,
                        Q_SEARCH: params.searchText,
                        Q_KEY: 'host,username,creator,modifier',
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
                {field: 'name', title: '名称', sortable: true, halign: 'center'},
                {field: 'host', title: '主机地址', sortable: true, halign: 'center'},
                {field: 'username', title: '登录用户名', sortable: true, halign: 'center'},
                {
                    field: 'runStatus', title: '运行状态', sortable: true, halign: 'center',
                    formatter: function (val, row, index) {

                        if (val == 0) {
                            return '<img src="/resources/images/stop.png" height="30" width="30"/>';
                        } else if (val == 1) {
                            return '<img src="/resources/images/running.png" height="30" width="30"/>';
                        }
                    }
                },
                {field: 'creator', title: '创建人', sortable: true, halign: 'center'},
                {field: 'modifier', title: '修改人', sortable: true, halign: 'center'},
                {
                    field: 'createTime', title: '创建时间', sortable: true, halign: 'center',
                    formatter: function (val, row, index) {
                        return dateFormatter(val);
                    }
                },
                {
                    field: 'updateTime', title: '修改时间', sortable: true, halign: 'center',
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
     * 点击查看主机密码
     */
    $('#cat-submit-btn').click(function () {
        var json = $('#cat-pwd-form').serializeObject();
        $.ajax({
            url: API_PATH + '/monitor/host/catPwd',
            type: 'post',
            data: json,
            success: function (res) {
                if (res.code == 200) {
                    $('#cat-pwd').prev('label').addClass('active');
                    $('#cat-pwd').val(res.data);
                } else {
                    $.message({
                        type: 'warning',
                        message: res.msg
                    })
                }
            }
        })
    })

    $('#add-save-btn').click(function () {
        var json = $('#add-form').serializeObject();
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

    window.actionEvents = {
        'click .cat-pwd': function (e, value, row, index) {
            $('#host-id').val(row.id);
            $('#cat-pwd-dialog').modal('show');
        },
        'click .edit': function (e, value, row, index) {
            $('#createDialog .modal-body label').attr('class', 'active');
            $('.modal-title').html('编辑主机');
            $('#add-name').val(row.name);
            $('#add-id').val(row.id);
            $('#add-host').val(row.host);
            $('#add-username').val(row.username);
            $('#add-password').val('******');
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
                                url: API_PATH + '/monitor/host/delete',
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
                                url: API_PATH + '/monitor/host/deleteBatchIds',
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
        '<a class="cat-pwd ml10" href="javascript:void(0)" data-toggle="tooltip" title="查看密码"><i class="glyphicon glyphicon-search"></i></a>　',
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
