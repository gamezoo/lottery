var systemNoticeVM = new Vue({
	el : '#system-notice',
	data : {
		noticeTitle : '',
		systemNoticeActionTitle : '',
		noticeContentEditor : null,
		addOrUpdateSystemNoticeFlag : false,
		editSystemNotice : {}
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.initTable();
		this.noticeContentEditor = new window.wangEditor('#noticeContentEditor');
		this.noticeContentEditor.customConfig.menus = [ 'head', // 标题
		'bold', // 粗体
		'fontSize', // 字号
		'fontName', // 字体
		'italic', // 斜体
		'underline', // 下划线
		'strikeThrough', // 删除线
		'foreColor', // 文字颜色
		'backColor', // 背景颜色
		'link', // 插入链接
		'list', // 列表
		'justify', // 对齐方式
		'quote', // 引用
		'emoticon', // 表情
		'table', // 表格
		'video', // 插入视频
		'code', // 插入代码
		'undo', // 撤销
		'redo' // 重复
		];
		this.noticeContentEditor.create();
	},
	methods : {

		showAddSystemNoticeModal : function() {
			this.addOrUpdateSystemNoticeFlag = true;
			this.systemNoticeActionTitle = '新增公告';
			this.editSystemNotice = {
				noticeTitle : '',
				publishDate : '',
				noticeContent : ''
			};
			this.noticeContentEditor.txt.html('');
		},

		showEditSystemNoticeModal : function(id) {
			var that = this;
			that.$http.get('/systemNotice/findSystemNoticeById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.editSystemNotice = res.body.data;
				that.addOrUpdateSystemNoticeFlag = true;
				that.systemNoticeActionTitle = '编辑公告';
				this.noticeContentEditor.txt.html(that.editSystemNotice.noticeContent);
			});
		},

		addOrUpdateSystemNotice : function() {
			var that = this;
			var editSystemNotice = that.editSystemNotice;
			if (editSystemNotice.noticeTitle == null || editSystemNotice.noticeTitle == '') {
				layer.alert('请输入公告标题', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editSystemNotice.publishDate == null || editSystemNotice.publishDate == '') {
				layer.alert('请输入发布日期', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var noticeContent = this.noticeContentEditor.txt.html();
			if (noticeContent == null || noticeContent == '') {
				layer.alert('请输入公告内容', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			editSystemNotice.noticeContent = noticeContent;
			that.$http.post('/systemNotice/addOrUpdateSystemNotice', editSystemNotice).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateSystemNoticeFlag = false;
				that.refreshTable();
			});
		},

		delSystemNotice : function(id) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/systemNotice/delSystemNoticeById', {
					params : {
						id : id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.refreshTable();
				});
			});
		},

		initTable : function() {
			var that = this;
			$('.system-notice-table').bootstrapTable({
				classes : 'table table-hover',
				height : 490,
				url : '/systemNotice/findSystemNoticeByPage',
				pagination : true,
				sidePagination : 'server',
				pageNumber : 1,
				pageSize : 10,
				pageList : [ 10, 25, 50, 100 ],
				queryParamsType : '',
				queryParams : function(params) {
					var condParam = {
						pageSize : params.pageSize,
						pageNum : params.pageNumber,
						noticeTitle : that.noticeTitle
					};
					return condParam;
				},
				responseHandler : function(res) {
					return {
						total : res.data.total,
						rows : res.data.content
					};
				},
				columns : [ {
					field : 'noticeTitle',
					title : '公告标题'
				}, {
					field : 'createTime',
					title : '创建时间'
				}, {
					field : 'publishDate',
					title : '发布日期'
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						return [ '<button type="button" class="edit-system-notice-btn btn btn-outline-success btn-sm" style="margin-right: 4px;">编辑</button>', '<button type="button" class="del-system-notice-btn btn btn-outline-danger btn-sm">删除</button>' ].join('');
					},
					events : {
						'click .edit-system-notice-btn' : function(event, value, row, index) {
							that.showEditSystemNoticeModal(row.id);
						},
						'click .del-system-notice-btn' : function(event, value, row, index) {
							that.delSystemNotice(row.id);
						}
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.system-notice-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},
	}
});