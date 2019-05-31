<template>
  <b-container fluid>
    <div class="top-alert">
      <b-alert class="inner-alert" variant="danger" dismissible
      :show="showErrorAlert" @dismissed="dismissErrorAlert">
        {{this.errorMsg}}
      </b-alert>
    </div>

    <h1>Employees</h1>
    <!-- User Interface controls -->
    <b-row>
      <b-col md="6" class="my-1">
        <b-form-group label-cols-sm="3" label="Filter" class="mb-0">
          <b-input-group>
            <b-form-input v-model="filter" placeholder="Type to Search" />
            <b-input-group-append>
              <b-button :disabled="!filter" @click="filter = ''">Clear</b-button>
            </b-input-group-append>
          </b-input-group>
        </b-form-group>
      </b-col>

      <b-col md="6" class="my-1">
        <b-form-group label-cols-sm="3" label="Sort" class="mb-0">
          <b-input-group>
            <b-form-select v-model="sortBy" :options="sortOptions">
              <option slot="first" :value="null">-- none --</option>
            </b-form-select>
            <b-form-select :disabled="!sortBy" v-model="sortDesc" slot="append">
              <option :value="false">Asc</option> <option :value="true">Desc</option>
            </b-form-select>
          </b-input-group>
        </b-form-group>
      </b-col>

      <b-col md="6" class="my-1">
        <b-form-group label-cols-sm="3" label="Sort direction" class="mb-0">
          <b-input-group>
            <b-form-select v-model="sortDirection" slot="append">
              <option value="asc">Asc</option> <option value="desc">Desc</option>
              <option value="last">Last</option>
            </b-form-select>
          </b-input-group>
        </b-form-group>
      </b-col>

      <b-col md="6" class="my-1">
        <b-form-group label-cols-sm="3" label="Per page" class="mb-0">
          <b-form-select :options="pageOptions" v-model="perPage" />
        </b-form-group>
      </b-col>
    </b-row>

    <!-- Main table element -->
    <b-table
      id="employeeTable"
      show-empty
      bordered
      hover
      striped
      stacked="md"
      :items="items"
      :fields="fields"
      :current-page="currentPage"
      :per-page="perPage"
      :filter="filter"
      :sort-by.sync="sortBy"
      :sort-desc.sync="sortDesc"
      :sort-direction="sortDirection"
      @filtered="onFiltered"
      @row-clicked="userInfoModal"
    >
    </b-table>

    <b-row>
      <b-col md="6" class="my-1">
        <b-pagination
          v-model="currentPage"
          :total-rows="totalRows"
          :per-page="perPage"
          class="my-0"
          aria-controls="employeeTable"
        />
      </b-col>
      <b-col md="6" v-if="this.loggedInRole==='ADMINISTRATOR'">
        <b-button
          class="float-right"
          variant="success"
          v-b-modal.createUserModal>
            Create Employee
        </b-button>
      </b-col>
    </b-row>

    <!-- Info modal -->
    <b-modal ref="infoUserModal" id="infoUserModal" title="Info Emyployee"
      size="lg" @hide="infoUserCancel"
      hide-footer hide-header-close centered>
      <b-form @submit="updateUser">
        <b-form-group v-if="this.loggedInRole === 'ADMINISTRATOR'"
          label-cols="4" label-cols-lg="2" label="ID"
          label-for="selectedId">
          <b-form-input id="selectedId"
            v-model="selectedUserId"
            disabled required>
          </b-form-input>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="First Name"
          label-for="selectedFirstName">
          <b-form-input id="selectedFirstName"
            v-model="selectedUserFirstName"
            v-bind:disabled="this.loggedInRole !== 'ADMINISTRATOR'" required>
          </b-form-input>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="Last Name"
          label-for="selectedLastName">
          <b-form-input id="selectedLastName"
            v-model="selectedUserLastName"
            v-bind:disabled="this.loggedInRole !== 'ADMINISTRATOR'" required>
          </b-form-input>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="E-Mail"
          label-for="selectedEmail">
          <b-form-input id="selectedEmail"
            v-model="selectedUserEmail"
            v-bind:disabled="this.loggedInRole !== 'ADMINISTRATOR'" required>
          </b-form-input>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="Role"
          label-for="selectedUserRole">
          <b-form-select v-model="selectedUserRole" :options="roleOptions"
          class="marg-bot" disabled required></b-form-select>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="Active"
          label-for="selectedActive">
          <b-form-checkbox id="selectedActive" v-model="selectedUserActive"
            v-bind:disabled="this.loggedInRole !== 'ADMINISTRATOR'" required>
          </b-form-checkbox>
        </b-form-group>
        <b-row>
          <b-col>
            <b-button v-if="this.loggedInRole === 'ADMINISTRATOR'"
              @click="infoUserDelete" variant="danger">Delete Employee</b-button>
          </b-col>
          <b-col>
            <b-button v-if="this.loggedInRole === 'ADMINISTRATOR'"
              variant="warning" class="float-right marg-left"
              type="submit">Update</b-button>
            <b-button @click="infoUserCancel" class="float-right">Cancel</b-button>
          </b-col>
        </b-row>
      </b-form>
    </b-modal>

    <!-- Create User Modal -->
    <b-modal ref="createUser" id="createUserModal"
      title="Create Employee" @hide="createUserModalCancel" hide-footer hide-header-close
      size="lg" centered>
      <b-form @submit="createUser">
        <b-form-group label-cols="4" label-cols-lg="2" label="First Name"
          label-for="firstName">
          <b-form-input v-model="createUserFirstName" id="firstName" class="marg-bot"
            placeholder="First name" required/>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="Last Name"
          label-for="lastName">
          <b-form-input v-model="createUserLastName" id="lastName" class="marg-bot"
            placeholder="Last name" required/>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="E-Mail"
          label-for="eMail">
          <b-form-input v-model="createUserEmail" id="eMail" class="marg-bot"
            type="email" placeholder="e@mail.com" required/>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="Password"
          label-for="password">
          <b-form-input v-model="createUserPw" id="password" class="marg-bot"
            type="password" placeholder="Password" required/>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="Role"
          label-for="userRole">
          <b-form-select v-model="createUserRole" :options="roleOptions"
            id="userRole" class="marg-bot" required></b-form-select>
        </b-form-group>
        <b-row class="marg-top">
          <b-col>
            <b-button variant="success" class="float-right" type="submit">Create</b-button>
            <b-button @click="createUserModalCancel"
            class="float-right marg-right">Cancel</b-button>
          </b-col>
        </b-row>
      </b-form>
    </b-modal>
  </b-container>
</template>


<script>
import axios from 'axios';

const items = [];

export default {
  name: 'Employees',

  props: {
    loggedInRole: String,
    loggedInId: String,
  },

  beforeMount() {
    if (localStorage.getItem('token') !== null) {
      this.restHeader = { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } };
      this.getUser();
    }
  },

  data() {
    return {
      // API
      ApiServer: process.env.VUE_APP_API_SERVER,
      ApiPort: process.env.VUE_APP_API_PORT,
      restHeader: null,
      errorMsg: '',
      showErrorAlert: false,
      // Table data
      items,
      fields: [
        {
          key: 'firstName',
          label: 'First Name',
          sortable: true,
        },
        {
          key: 'lastName',
          label: 'Last Name',
          sortable: true,
        },
        {
          key: 'emailAddress',
          label: 'Email',
          sortable: true,
          sortDirection: 'desc',
        },
        {
          key: 'active',
          label: 'Is Active',
          sortable: true,
          sortDirection: 'desc',
        },
        {
          key: 'role',
          label: 'Role',
        },
      ],
      currentPage: 1,
      perPage: 5,
      totalRows: 0,
      pageOptions: [5, 10, 15],
      sortBy: null,
      sortDesc: false,
      sortDirection: 'asc',
      filter: null,
      // Create user data
      createUserFirstName: '',
      createUserLastName: '',
      createUserEmail: '',
      createUserPw: '',
      createUserRole: null,
      roleOptions: [
        { value: null, text: 'Role', disabled: true },
        { value: 'ADMINISTRATOR', text: 'Administrator' },
        { value: 'PROJECTMANAGER', text: 'Projectmanager' },
        { value: 'DEVELOPER', text: 'Developer' },
      ],
      // Info user data
      selectedUserId: '',
      selectedUserFirstName: '',
      selectedUserLastName: '',
      selectedUserEmail: '',
      selectedUserRole: '',
      selectedUserActive: false,
    };
  },
  computed: {
    sortOptions() {
      // Create an options list from our fields
      return this.fields
        .filter(f => f.sortable)
        .map(f => ({ text: f.label, value: f.key }));
    },
  },
  methods: {
    info(item, index, button) {
      this.modalInfo.title = `Row index: ${index}`;
      this.modalInfo.content = JSON.stringify(item, null, 2);
      this.$root.$emit('bv::show::modal', 'modalInfo', button);
    },
    resetModal() {
      this.modalInfo.title = '';
      this.modalInfo.content = '';
    },
    onFiltered(filteredItems) {
      // Trigger pagination to update the number of buttons/pages due to filtering
      this.totalRows = filteredItems.length;
      this.currentPage = 1;
    },
    getUser() {
      if (this.loggedInRole === 'PROJECTMANAGER') {
        axios.get(`${this.ApiServer}:${this.ApiPort}/api/employee?role=DEVELOPER`, this.restHeader)
          .then((response) => {
            this.items = response.data;
            this.totalRows = this.items.length;
          });
      } else {
        axios.get(`${this.ApiServer}:${this.ApiPort}/api/employee`, this.restHeader)
          .then((response) => {
            if (this.loggedInRole === 'DEVELOPER') {
              for (let i = 0; i < response.data.length; i += 1) {
                if (response.data[i].id === this.loggedInId) {
                  this.items = [response.data[i]];
                  break;
                }
              }
            } else {
              this.items = response.data;
              this.totalRows = this.items.length;
            }
          });
      }
    },
    createUser(evt) {
      evt.preventDefault();

      const data = {
        active: true,
        firstName: this.createUserFirstName,
        lastName: this.createUserLastName,
        emailAddress: this.createUserEmail,
      };

      axios.post(`${this.ApiServer}:${this.ApiPort}/api/employee?password=${this.createUserPw}&role=${this.createUserRole}`, data, this.restHeader)
        .then((response) => {
          const newUser = {
            id: response.data.id,
            firstName: response.data.firstName,
            lastName: response.data.lastName,
            emailAddress: response.data.emailAddress,
            active: true,
            role: response.data.role,
          };

          this.items.unshift(newUser);
          this.totalRows = this.items.length;

          this.createUserModalCancel();
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    createUserModalCancel() {
      this.createUserFirstName = '';
      this.createUserLastName = '';
      this.createUserEmail = '';
      this.createUserPw = '';
      this.createUserRole = null;
      this.$refs.createUser.hide();
    },
    userInfoModal(evt) {
      this.selectedUserId = evt.id;
      this.selectedUserFirstName = evt.firstName;
      this.selectedUserLastName = evt.lastName;
      this.selectedUserEmail = evt.emailAddress;
      this.selectedUserRole = evt.role;
      this.selectedUserActive = evt.active;

      this.$refs.infoUserModal.show();
    },
    updateUser(evt) {
      evt.preventDefault();

      const data = {
        active: this.selectedUserActive,
        firstName: this.selectedUserFirstName,
        lastName: this.selectedUserLastName,
        emailAddress: this.selectedUserEmail,
      };

      axios.put(`${this.ApiServer}:${this.ApiPort}/api/employee/${this.selectedUserId}`, data, this.restHeader)
        // eslint-disable-next-line
        .then((response) => {
          for (let i = 0; i < this.items.length; i += 1) {
            if (this.items[i].id === this.selectedUserId) {
              this.items[i].firstName = this.selectedUserFirstName;
              this.items[i].lastName = this.selectedUserLastName;
              this.items[i].emailAddress = this.selectedUserEmail;
              this.items[i].active = this.selectedUserActive;
              this.items[i].role = this.selectedUserRole;
            }
          }

          this.infoUserCancel();
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    infoUserDelete() {
      axios.delete(`${this.ApiServer}:${this.ApiPort}/api/employee/${this.selectedUserId}`, this.restHeader)
        // eslint-disable-next-line
        .then((response) => {
          for (let i = 0; i < this.items.length; i += 1) {
            if (this.items[i].id === this.selectedUserId) {
              this.items.splice(i, 1);
              break;
            }
          }

          this.totalRows = this.items.length;
          this.infoUserCancel();
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    infoUserCancel() {
      this.selectedUserId = null;
      this.selectedUserFirstName = '';
      this.selectedUserLastName = '';
      this.selectedUserEmail = '';
      this.selectedUserRole = null;

      this.$refs.infoUserModal.hide();
    },
    dismissErrorAlert() {
      this.showErrorAlert = false;
    },
    errorHandler(error) {
      this.errorMsg = error.response.data;
      this.showErrorAlert = true;
    },
  },
};
</script>

<style scoped>
.marg-bot {
  margin-bottom: 5px;
}

.marg-right {
  margin-right: 5px;
}

.marg-left {
  margin-left: 5px;
}

.marg-top {
  margin-top: 5px;
}

.top-alert {
  position: fixed;
  top: 15px;
  width: calc(100% - 30px);
  z-index: 999999;
}

.inner-alert {
  margin: 0;
}
</style>
