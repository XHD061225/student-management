<template>
  <main class="page-shell">
    <header class="topbar">
      <a class="brand" href="#" aria-label="学生管理首页">
        <span class="brand-mark">学</span>
        <span>校园管理系统</span>
      </a>
      <span class="topbar-note">学生信息管理</span>
    </header>

    <section class="content">
      <div class="page-heading">
        <div>
          <p class="eyebrow">STUDENT DIRECTORY</p>
          <h1>学生信息</h1>
          <p class="subtitle">集中管理学生档案，快速查找和维护信息。</p>
        </div>
        <button class="button button-primary" type="button" @click="openCreate">＋ 新增学生</button>
      </div>

      <div class="toolbar card">
        <label class="search-box">
          <span aria-hidden="true">⌕</span>
          <input v-model="keyword" type="search" placeholder="搜索姓名、学号或班级" @keyup.enter="search">
        </label>
        <button class="button button-light" type="button" @click="search">搜索</button>
        <span class="result-count">共 {{ total }} 位学生</span>
      </div>

      <div v-if="error" class="alert" role="alert">{{ error }}</div>
      <div class="card table-card">
        <div v-if="loading" class="empty-state">正在加载学生信息…</div>
        <div v-else-if="students.length === 0" class="empty-state">
          <span class="empty-icon">▤</span>
          <strong>还没有学生信息</strong>
          <span>新增一条记录，开始管理学生档案。</span>
          <button class="button button-primary" type="button" @click="openCreate">新增学生</button>
        </div>
        <div v-else class="table-scroll">
          <table>
            <thead>
              <tr><th>学生</th><th>学号</th><th>班级</th><th>性别</th><th>联系电话</th><th>操作</th></tr>
            </thead>
            <tbody>
              <tr v-for="student in students" :key="student.id">
                <td><div class="student-name"><span class="avatar">{{ student.name.slice(0, 1) }}</span><strong>{{ student.name }}</strong></div></td>
                <td class="muted">{{ student.student_no }}</td>
                <td><span class="class-badge">{{ student.class_name }}</span></td>
                <td>{{ student.gender || '—' }}</td>
                <td class="muted">{{ student.phone || '—' }}</td>
                <td class="actions">
                  <button class="text-button" type="button" @click="openEdit(student)">编辑</button>
                  <button class="text-button danger-text" type="button" @click="removeStudent(student)">删除</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <footer v-if="lastPage > 1" class="pagination">
          <span>第 {{ currentPage }} / {{ lastPage }} 页</span>
          <div>
            <button class="button button-light" type="button" :disabled="currentPage <= 1" @click="loadStudents(currentPage - 1)">上一页</button>
            <button class="button button-light" type="button" :disabled="currentPage >= lastPage" @click="loadStudents(currentPage + 1)">下一页</button>
          </div>
        </footer>
      </div>
    </section>

    <div v-if="formOpen" class="modal-backdrop" @click.self="closeForm">
      <section class="modal card" role="dialog" aria-modal="true" :aria-label="editing ? '编辑学生' : '新增学生'">
        <div class="modal-heading">
          <div><p class="eyebrow">STUDENT PROFILE</p><h2>{{ editing ? '编辑学生' : '新增学生' }}</h2></div>
          <button class="close-button" type="button" aria-label="关闭" @click="closeForm">×</button>
        </div>
        <form @submit.prevent="saveStudent">
          <div class="form-grid">
            <label>姓名 <span class="required">*</span><input v-model.trim="form.name" required maxlength="80" placeholder="请输入姓名"></label>
            <label>学号 <span class="required">*</span><input v-model.trim="form.student_no" required maxlength="32" placeholder="请输入学号"></label>
            <label>班级 <span class="required">*</span><input v-model.trim="form.class_name" required maxlength="100" placeholder="例如：计算机 1 班"></label>
            <label>性别<select v-model="form.gender"><option value="">请选择</option><option>男</option><option>女</option><option>其他</option></select></label>
            <label>联系电话<input v-model.trim="form.phone" maxlength="30" placeholder="请输入联系电话"></label>
            <label>邮箱<input v-model.trim="form.email" type="email" maxlength="150" placeholder="请输入邮箱"></label>
          </div>
          <p v-if="formError" class="form-error">{{ formError }}</p>
          <div class="modal-actions"><button class="button button-light" type="button" @click="closeForm">取消</button><button class="button button-primary" type="submit" :disabled="saving">{{ saving ? '保存中…' : '保存信息' }}</button></div>
        </form>
      </section>
    </div>
  </main>
</template>

<script>
import axios from 'axios'

const api = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8000/api',
  headers: { Accept: 'application/json' }
})

const emptyForm = () => ({ student_no: '', name: '', gender: '', class_name: '', phone: '', email: '' })

export default {
  name: 'App',
  data() {
    return {
      students: [], keyword: '', total: 0, currentPage: 1, lastPage: 1,
      loading: false, saving: false, error: '', formError: '', formOpen: false,
      editing: null, form: emptyForm()
    }
  },
  mounted() {
    this.loadStudents()
  },
  methods: {
    async loadStudents(page = 1) {
      this.loading = true
      this.error = ''
      try {
        const { data } = await api.get('/students', { params: { q: this.keyword, page, per_page: 10 } })
        this.students = data.data
        this.total = data.total
        this.currentPage = data.current_page
        this.lastPage = data.last_page
      } catch (error) {
        this.error = this.getErrorMessage(error, '加载失败，请确认 Laravel API 已启动。')
      } finally {
        this.loading = false
      }
    },
    search() {
      this.loadStudents(1)
    },
    openCreate() {
      this.editing = null
      this.form = emptyForm()
      this.formError = ''
      this.formOpen = true
    },
    openEdit(student) {
      this.editing = student.id
      this.form = { ...emptyForm(), ...student }
      this.formError = ''
      this.formOpen = true
    },
    closeForm() {
      this.formOpen = false
      this.formError = ''
    },
    async saveStudent() {
      this.saving = true
      this.formError = ''
      try {
        if (this.editing) {
          await api.put(`/students/${this.editing}`, this.form)
        } else {
          await api.post('/students', this.form)
        }
        this.closeForm()
        await this.loadStudents(this.currentPage)
      } catch (error) {
        this.formError = this.getErrorMessage(error, '保存失败，请稍后重试。')
      } finally {
        this.saving = false
      }
    },
    async removeStudent(student) {
      if (!window.confirm(`确定删除学生“${student.name}”吗？此操作无法撤销。`)) return
      this.error = ''
      try {
        await api.delete(`/students/${student.id}`)
        const page = this.students.length === 1 && this.currentPage > 1 ? this.currentPage - 1 : this.currentPage
        await this.loadStudents(page)
      } catch (error) {
        this.error = this.getErrorMessage(error, '删除失败，请稍后重试。')
      }
    },
    getErrorMessage(error, fallback) {
      const data = error.response && error.response.data
      if (data && data.errors) return Object.values(data.errors).flat()[0]
      if (data && data.message) return data.message
      return fallback
    }
  }
}
</script>

<style>
:root { font-family: Inter, -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC", "Microsoft YaHei", sans-serif; color: #172033; background: #f5f7fb; font-synthesis: none; }
* { box-sizing: border-box; }
body { margin: 0; min-width: 320px; background: #f5f7fb; }
button, input, select { font: inherit; }
button { cursor: pointer; }
button:disabled { cursor: not-allowed; opacity: .5; }
.page-shell { min-height: 100vh; }
.topbar { height: 68px; display: flex; align-items: center; justify-content: space-between; padding: 0 max(calc((100vw - 1120px) / 2), 28px); background: #fff; border-bottom: 1px solid #e9edf4; }
.brand { display: flex; align-items: center; gap: 10px; color: #172033; font-weight: 700; text-decoration: none; }
.brand-mark { display: grid; width: 34px; height: 34px; place-items: center; border-radius: 10px; color: #fff; background: #4f6bed; }
.topbar-note, .muted, .result-count { color: #7a8497; font-size: 13px; }
.content { max-width: 1120px; margin: 0 auto; padding: 48px 24px 72px; }
.page-heading { display: flex; align-items: center; justify-content: space-between; gap: 24px; margin-bottom: 28px; }
.eyebrow { margin: 0 0 8px; color: #7484c5; font-size: 10px; font-weight: 700; letter-spacing: 1.6px; }
h1 { margin: 0; font-size: 30px; letter-spacing: -.7px; }
.subtitle { margin: 9px 0 0; color: #788398; font-size: 14px; }
.button { min-height: 38px; padding: 0 15px; border: 1px solid transparent; border-radius: 8px; font-size: 13px; font-weight: 600; transition: .15s ease; }
.button-primary { color: #fff; background: #4f6bed; box-shadow: 0 3px 8px #4f6bed30; }
.button-primary:hover { background: #405bd7; }
.button-light { color: #47536a; background: #fff; border-color: #e2e7ef; }
.button-light:hover { background: #f7f8fb; }
.card { background: #fff; border: 1px solid #e8ecf3; border-radius: 12px; box-shadow: 0 4px 16px #27334d06; }
.toolbar { display: flex; align-items: center; gap: 10px; padding: 14px 16px; margin-bottom: 16px; }
.search-box { display: flex; align-items: center; gap: 9px; width: min(390px, 60%); height: 38px; padding: 0 11px; color: #8d97a8; border: 1px solid #e5e9f0; border-radius: 8px; }
.search-box input { width: 100%; border: 0; outline: 0; color: #172033; background: transparent; font-size: 13px; }
.result-count { margin-left: auto; }
.table-card { overflow: hidden; }
.table-scroll { overflow-x: auto; }
table { width: 100%; border-collapse: collapse; text-align: left; font-size: 13px; }
th { padding: 15px 20px; color: #8992a2; background: #fafbfc; font-size: 11px; font-weight: 600; white-space: nowrap; }
td { padding: 15px 20px; border-top: 1px solid #f0f2f6; white-space: nowrap; }
.student-name { display: flex; align-items: center; gap: 10px; }
.student-name strong { font-weight: 600; }
.avatar { display: grid; width: 32px; height: 32px; place-items: center; border-radius: 50%; color: #5169d9; background: #edf0ff; font-weight: 700; }
.class-badge { padding: 5px 9px; color: #596a9a; background: #f0f3fb; border-radius: 6px; font-size: 12px; }
.actions { text-align: right; }
.text-button { padding: 4px 6px; border: 0; color: #4f6bed; background: transparent; font-size: 12px; }
.danger-text { margin-left: 6px; color: #db5c62; }
.empty-state { min-height: 270px; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 12px; color: #8992a2; font-size: 13px; }
.empty-state strong { color: #303b50; font-size: 15px; }
.empty-icon { color: #aab4ca; font-size: 34px; }
.empty-state .button { margin-top: 4px; }
.alert { margin-bottom: 14px; padding: 12px 14px; color: #9c3d42; background: #fff0f0; border: 1px solid #ffdada; border-radius: 8px; font-size: 13px; }
.pagination { display: flex; align-items: center; justify-content: space-between; padding: 14px 18px; border-top: 1px solid #f0f2f6; color: #7a8497; font-size: 12px; }
.pagination div { display: flex; gap: 8px; }
.pagination .button { min-height: 32px; padding: 0 11px; font-size: 12px; }
.modal-backdrop { position: fixed; inset: 0; z-index: 5; display: grid; place-items: center; padding: 20px; background: #10182b70; }
.modal { width: min(560px, 100%); padding: 24px; box-shadow: 0 20px 70px #10182b30; }
.modal-heading { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px; }
.modal-heading h2 { margin: 0; font-size: 20px; }
.close-button { width: 30px; height: 30px; border: 0; border-radius: 7px; color: #7a8497; background: #f4f6f9; font-size: 22px; }
.form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 17px 14px; }
.form-grid label { display: flex; flex-direction: column; gap: 7px; color: #47536a; font-size: 12px; font-weight: 600; }
.form-grid input, .form-grid select { width: 100%; height: 39px; padding: 0 10px; border: 1px solid #e2e7ef; border-radius: 7px; outline: none; color: #172033; background: #fff; font-size: 13px; font-weight: 400; }
.form-grid input:focus, .form-grid select:focus { border-color: #8293f4; box-shadow: 0 0 0 3px #4f6bed16; }
.required, .form-error { color: #d84f56; }
.form-error { margin: 14px 0 0; font-size: 12px; }
.modal-actions { display: flex; justify-content: flex-end; gap: 9px; margin-top: 25px; padding-top: 17px; border-top: 1px solid #f0f2f6; }
@media (max-width: 640px) { .topbar { padding: 0 18px; } .topbar-note { display: none; } .content { padding: 32px 16px 48px; } .page-heading { align-items: flex-start; } h1 { font-size: 25px; } .subtitle { max-width: 220px; line-height: 1.6; } .page-heading > .button { flex-shrink: 0; padding: 0 10px; } .toolbar { flex-wrap: wrap; } .search-box { flex: 1; width: auto; } .result-count { order: 2; width: 100%; margin: 4px 0 0; } th, td { padding: 13px 14px; } .form-grid { grid-template-columns: 1fr; gap: 13px; } .modal { padding: 20px; } }
</style>
