<template>
    <div>
        <el-card class="border">
            <el-form :inline="true" :model="searchForm">
                <el-row>
                    <el-col :span="20">
                        <el-form-item label="姓名">
                            <el-input v-model="searchForm.username" placeholder="请输入姓名进行查询"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="onSearch">查询</el-button>
                        </el-form-item>
                    </el-col>
                    <el-col :span="4">
                        <el-form-item>
                            <!-- <el-button type="primary" icon="el-icon-circle-plus-outline" @click="onAddUser"
                                circle></el-button> -->
                            <i class="el-icon-circle-plus-outline" @click="onAddUser" style="font-size: 30px;"></i>
                        </el-form-item>
                    </el-col>
                </el-row>

            </el-form>
        </el-card>
        <el-card class="border">
            <el-table :data="tableData" border style="width: 100%">
                <el-table-column prop="id" label="用户id" width="180">
                </el-table-column>
                <el-table-column prop="username" label="姓名" width="180">
                </el-table-column>
                <el-table-column prop="email" label="邮箱">
                </el-table-column>
                <el-table-column prop="phone" label="手机号">
                </el-table-column>
                <el-table-column prop="status" label="状态">
                </el-table-column>
                <el-table-column align="right">
                    <template slot-scope="scope">
                        <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button size="mini" type="danger"
                            @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>
        <el-card>
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="currentPage" :page-sizes="[5, 20, 50, 100]" :page-size="5"
                layout="total, sizes, prev, pager, next, jumper" :total="total">
            </el-pagination>
        </el-card>

        <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible">
            <el-form :rules='rules' :model="dialogForm" ref="dialogFormRef">
                <el-form-item label="用户名称" prop="username">
                    <el-input v-model="dialogForm.username" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="dialogForm.password" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="dialogForm.email" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                    <el-input v-model="dialogForm.phone" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-radio v-model="dialogForm.status" label="1">启用</el-radio>
                    <el-radio v-model="dialogForm.status" label="0">禁用</el-radio>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="cancelDialogForm">取 消</el-button>
                <el-button type="primary" @click="submitDialogForm">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="请确认" :visible.sync="deleteDialogVisible" width="30%">
            <span>确认要删除该用户吗？</span>
            <span slot="footer" class="dialog-footer">
                <el-button @click="cancelDelete">取 消</el-button>
                <el-button type="primary" @click="confirmDelete">确 定</el-button>
            </span>
        </el-dialog>
    </div>

</template>

<script>
import { getUserList, addUser, getUserById, updateUser, removeByUserId } from '@/api/userManagerApi'

export default {
    data() {
        return {
            searchForm: {
                username: '',
            },
            addOrEdit: 'add',
            tableData: [],
            total: 0,
            currentPage: 1,
            pageSize: 5,
            dialogTitle: '新增用户',
            dialogFormVisible: false,
            deleteDialogVisible: false,
            dialogForm: {
                username: '',
                password: '',
                email: '',
                phone: '',
                status: '1'
            },
            rules: {
                username: [
                    { required: true, message: '请输入用户名称', trigger: 'blur' },
                    { min: 5, max: 10, message: '长度在 5 到 10 个字符', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'change' }
                ]
            },
            toDeleteUserInfo: null
        }
    },
    methods: {
        async onSearch() {
            let res = await getUserList({
                pageNo: this.currentPage,
                pageSize: this.pageSize,
                username: this.searchForm.username,
            });
            console.log(res);
            this.tableData = res.data.rows;
            this.total = res.data.total;
        },

        handleSizeChange(val) {
            this.pageSize = val;
            this.onSearch();

        },
        handleCurrentChange(val) {
            this.currentPage = val;
            this.onSearch();
        },
        clearForm() {
            this.dialogForm = {};

        },
        onAddUser() {
            this.addOrEdit = 'add';
            this.dialogFormVisible = true;

        },
        cancelDialogForm() {
            this.dialogFormVisible = false;
            this.$refs.dialogFormRef.clearValidate();
            this.clearForm();
        },
        async submitDialogForm() {
            if (this.addOrEdit === 'add') {
                this.$refs['dialogFormRef'].validate(async (valid) => {
                    if (valid) {
                        let res = await addUser({
                            username: this.dialogForm.username,
                            password: this.dialogForm.password,
                            email: this.dialogForm.email,
                            phone: this.dialogForm.phone,
                            status: this.dialogForm.status,
                        });
                        console.log(res);
                        this.$refs.dialogFormRef.clearValidate();
                        this.clearForm();
                        this.onSearch();
                        this.$message('新增成功！');
                        this.dialogFormVisible = false;

                    } else {
                        this.$message('校验不通过');
                        return false;
                    }
                });
            } else {
                // 发送编辑保存的请求
                this.$refs['dialogFormRef'].validate(async (valid) => {
                    if (valid) {
                        let res = await updateUser({
                            id: this.dialogForm.id,
                            username: this.dialogForm.username,
                            password: this.dialogForm.password,
                            email: this.dialogForm.email,
                            phone: this.dialogForm.phone,
                            status: this.dialogForm.status,
                        });
                        console.log(res);
                        this.$refs.dialogFormRef.clearValidate();
                        this.clearForm();
                        this.onSearch();
                        this.$message('修改成功！');
                        this.dialogFormVisible = false;
                    } else {
                        this.$message('校验不通过');
                        return false;
                    }
                });
            }

        },
        async handleEdit(id, user) {
            // 调用后台根据id查询数据
            let res = await getUserById(user.id);

            // 展示窗口
            this.addOrEdit = 'edit';
            this.dialogFormVisible = true;
            this.dialogForm = {
                id: res.data.id,
                username: res.data.username,
                password: res.data.password,
                email: res.data.email,
                phone: res.data.phone,
                status: res.data.status
            }

        },
        async handleDelete(id, user) {
            this.deleteDialogVisible = true;
            this.toDeleteUserInfo = user;
        },
        async confirmDelete() {
            // 调用后台
            await removeByUserId(this.toDeleteUserInfo.id);
            this.onSearch();
            this.$message('删除成功！');
            this.toDeleteUserInfo = null;
            this.deleteDialogVisible = false;
        },
        cancelDelete() {
            this.deleteDialogVisible = false;
            this.toDeleteUserInfo = null;
        }
    },
    created() {
        this.onSearch();
    }
}
</script>

<style>
.border {
    border-radius: 4px
}
</style>
