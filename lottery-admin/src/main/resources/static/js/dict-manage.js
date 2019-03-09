var dictManageVM = new Vue({
	el : '#dict-manage',
	data : {
		dictTypeCode : '',
		dictTypeName : '',

		editDictDataFlag : false,
		dictTypeId : '',
		dictItems : [],

		addOrUpdateDictTypeFlag : false,
		dictTypeActionTitle : '',
		editDictType : {}
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.initTable();
	},
	methods : {

		initTable : function() {
			var that = this;
			$('.dict-manage-table').bootstrapTable({
				classes : 'table table-hover',
				height : 490,
				url : '/dictconfig/findDictTypeByPage',
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
						dictTypeCode : that.dictTypeCode,
						dictTypeName : that.dictTypeName
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
					field : 'dictTypeCode',
					title : '字典code'
				}, {
					field : 'dictTypeName',
					title : '字典名称'
				}, {
					field : 'note',
					title : '备注'
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						return [ '<button type="button" class="edit-dict-data-btn btn btn-outline-primary btn-sm" style="margin-right: 4px;">编辑字典数据</button>', '<button type="button" class="edit-dict-type-btn btn btn-outline-success btn-sm" style="margin-right: 4px;">编辑</button>', '<button type="button" class="del-dict-type-btn btn btn-outline-danger btn-sm">删除</button>' ].join('');
					},
					events : {
						'click .edit-dict-data-btn ' : function(event, value, row, index) {
							that.openEditDictDataModal(row.id);
						},
						'click .edit-dict-type-btn' : function(event, value, row, index) {
							that.openEditDictTypeModal(row.id);
						},
						'click .del-dict-type-btn' : function(event, value, row, index) {
							that.delDictType(row.id);
						}
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.dict-manage-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		openEditDictDataModal : function(id) {
			var that = this;
			that.dictTypeId = id;
			that.$http.get('/dictconfig/findDictItemByDictTypeId', {
				params : {
					dictTypeId : id
				}
			}).then(function(res) {
				that.dictItems = res.body.data;
				this.editDictDataFlag = true;
			});
		},

		addDictItem : function() {
			this.dictItems.push({
				dictItemCode : '',
				dictItemName : ''
			});
		},

		delDictItem : function(index) {
			this.dictItems.splice(index, 1);
		},

		moveUpDictItem : function(index) {
			var temp = this.dictItems[index - 1];
			Vue.set(this.dictItems, index - 1, this.dictItems[index])
			Vue.set(this.dictItems, index, temp)
		},

		moveDownDictItem : function(index) {
			var i = this.dictItems[index + 1];
			Vue.set(this.dictItems, index + 1, this.dictItems[index])
			Vue.set(this.dictItems, index, i)
		},

		updateDictData : function() {
			var that = this;
			var dictItemCodeMap = new Map();
			var dictItems = that.dictItems;
			for (var i = 0; i < dictItems.length; i++) {
				var dictItem = dictItems[i];
				if (dictItem.dictItemCode == null || dictItem.dictItemCode == '') {
					layer.alert('请输入字典项code', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (dictItem.dictItemName == null || dictItem.dictItemName == '') {
					layer.alert('请输入字典项名称', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (dictItemCodeMap.get(dictItem.dictItemCode) != null) {
					layer.alert('字典项code不能重复', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				dictItemCodeMap.set(dictItem.dictItemCode, dictItem.dictItemCode);
			}
			that.$http.post('/dictconfig/updateDictData', {
				dictTypeId : that.dictTypeId,
				dictDatas : dictItems
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.editDictDataFlag = false;
				that.refreshTable();
			});
		},

		openAddDictTypeModal : function() {
			this.addOrUpdateDictTypeFlag = true;
			this.dictTypeActionTitle = '新增字典';
			this.editDictType = {
				dictTypeCode : '',
				dictTypeName : '',
				note : ''
			};
		},

		openEditDictTypeModal : function(id) {
			var that = this;
			that.$http.get('/dictconfig/findDictTypeById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.editDictType = res.body.data;
				that.addOrUpdateDictTypeFlag = true;
				that.dictTypeActionTitle = '编辑字典';
			});
		},

		addOrUpdateDictType : function() {
			var that = this;
			var editDictType = that.editDictType;
			if (editDictType.dictTypeCode == null || editDictType.dictTypeCode == '') {
				layer.alert('请输入字典code', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editDictType.dictTypeName == null || editDictType.dictTypeName == '') {
				layer.alert('请输入字典名称', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/dictconfig/addOrUpdateDictType', editDictType).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateDictTypeFlag = false;
				that.refreshTable();
			});
		},

		delDictType : function(id) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/dictconfig/delDictTypeById', {
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
		}
	}
});