<template>
  <b-container fluid>
    <div class="top-alert">
      <b-alert class="inner-alert" variant="danger" dismissible
      :show="showErrorAlert" @dismissed="dismissErrorAlert">
        {{this.errorMsg}}
      </b-alert>
    </div>

    <h1>Contracts</h1>

    <h4>Data filter</h4>
    <b-row>
      <b-col>
        <b-form-group label-cols="2" label="From Date">
          <b-form-input type="date" v-bind:max="filterToDate" v-model="filterFromDate">
          </b-form-input>
        </b-form-group>
      </b-col>
      <b-col>
        <b-form-group label-cols="2" label="To Date">
          <b-form-input type="date" v-bind:min="filterFromDate" v-model="filterToDate">
          </b-form-input>
        </b-form-group>
      </b-col>
      <b-col md="2">
        <b-button variant="primary" class="float-right"
          @click="applyFilter">Filter</b-button>
        <b-button class="float-right marg-right" @click="resetFilter">Reset</b-button>
      </b-col>
    </b-row>
    <h4>Table filter</h4>
    <b-row>
      <b-col md="6" class="my-1">
        <b-form-group label-cols-sm="3" label="Filter" class="mb-0">
          <b-input-group>
            <b-form-input v-model="filter" placeholder="Type to Search"/>
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
              <option :value="false">Asc</option>
              <option :value="true">Desc</option>
            </b-form-select>
          </b-input-group>
        </b-form-group>
      </b-col>

      <b-col md="6" class="my-1">
        <b-form-group label-cols-sm="3" label="Sort direction" class="mb-0">
          <b-input-group>
            <b-form-select v-model="sortDirection" slot="append">
              <option value="asc">Asc</option>
              <option value="desc">Desc</option>
              <option value="last">Last</option>
            </b-form-select>
          </b-input-group>
        </b-form-group>
      </b-col>

      <b-col md="6" class="my-1">
        <b-form-group label-cols-sm="3" label="Per page" class="mb-0">
          <b-form-select :options="pageOptions" v-model="perPage"/>
        </b-form-group>
      </b-col>
    </b-row>

    <!-- Main table element -->
    <b-table
      id="contractTable"
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
      @row-clicked="infoContractModal"
    >
      <!--
      <template slot="name" slot-scope="row">
        {{ row.value.first }} {{ row.value.last }}
      </template>

      <template slot="isActive" slot-scope="row">
        {{ row.value ? 'Yes' : 'No' }}
      </template>

      <template slot="actions" slot-scope="row">
        <b-button size="sm" @click="info(row.item, row.index, $event.target)" class="mr-1">
          Info modal
        </b-button>
        <b-button size="sm" @click="row.toggleDetails">
          {{ row.detailsShowing ? 'Hide' : 'Show' }} Details
        </b-button>
      </template>

      <template slot="row-details" slot-scope="row">
        <b-card>
          <ul>
            <li v-for="(value, key) in row.item" :key="key">{{ key }}: {{ value }}</li>
          </ul>
        </b-card>
      </template>
      -->
      <template slot="graph" slot-scope="row">
        <b-button size="sm" @click="modalGraph(row.item)" class="mr-1"
                  variant="primary">
          Contract Calendar
        </b-button>
      </template>
    </b-table>

    <b-row>
      <b-col md="6" class="my-1">
        <b-pagination
          :total-rows="totalRows"
          :per-page="perPage"
          v-model="currentPage"
          class="my-0"
        />
      </b-col>
      <b-col md="6" v-if="this.loggedInRole==='ADMINISTRATOR'">
        <b-button
          class="float-right"
          variant="success"
          @click="getEmployees"
          v-b-modal.createContractModal>
          Create Contract
        </b-button>
      </b-col>
    </b-row>

    <!-- Info modal
    <b-modal id="modalInfo" @hide="resetModal" :title="modalInfo.title" ok-only>
      <pre>{{ modalInfo.content }}</pre>
    </b-modal> -->

    <!-- Info Contract Modal -->
    <b-modal ref="infoContractModal" id="infoContractModal" :title=this.infoTitle
             size="lg" @hide="infoContractCancel"
             hide-footer hide-header-close centered>
      <b-form @submit="updateContract">
        <b-form-group v-if="this.loggedInRole === 'ADMINISTRATOR'"
                      label-cols="4" label-cols-lg="2" label="ID"
                      label-for="selectedId4">
          <b-form-input id="selectedId4"
                        v-model="selectedContractId"
                        disabled required>
          </b-form-input>
        </b-form-group>

        <b-form-group label-cols="4" label-cols-lg="2" label="Start Date"
                      label-for="selectedStartDate">
          <b-form-input id="selectedStartDate" type="date"
                        v-model="selectedContractStartDate"
                        v-bind:disabled="this.loggedInRole !== 'ADMINISTRATOR'"
                        v-bind:max="selectedContractEndDate" required>
          </b-form-input>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="End Date"
                      label-for="selectedEndDate">
          <b-form-input id="selectedEndDate" type="date"
                        v-model="selectedContractEndDate"
                        v-bind:disabled="this.loggedInRole !== 'ADMINISTRATOR'"
                        v-bind:min="selectedContractStartDate" required>
          </b-form-input>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="Pensum"
                      label-for="selectedPensum">
          <b-form-input id="selectedPensum"
                        v-model="selectedContractPensum"
                        v-bind:disabled="this.loggedInRole !== 'ADMINISTRATOR'" required>
          </b-form-input>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="Employee Id"
                      label-for="selectedEmployeeId">
          <b-form-input v-model="selectedContractEmployeeId"
                        class="marg-bot" disabled required></b-form-input>
        </b-form-group>
        <b-row>
          <b-col>
            <b-button v-if="this.loggedInRole === 'ADMINISTRATOR'"
                      @click="infoContractDelete" variant="danger">Delete Contract
            </b-button>
          </b-col>
          <b-col>
            <b-button v-if="this.loggedInRole === 'ADMINISTRATOR'"
                      variant="warning" class="float-right marg-left"
                      type="submit">Update
            </b-button>
            <b-button @click="infoContractCancel" class="float-right">Cancel</b-button>
          </b-col>
        </b-row>
      </b-form>
    </b-modal>

    <!-- Create Contract Modal -->
    <b-modal ref="createContract" id="createContractModal"
             title="Create Contract" @hide="createContractModalCancel" hide-footer
             hide-header-close
             size="lg"
             centered>
      <b-form @submit="createContract">
        <b-form-group label-cols="4" label-cols-lg="2" label="Start Date"
                      label-for="startDate9">
          <b-form-input v-model="createContractStartDate" id="startDate9" class="marg-bot"
                        type="date" placeholder="Start Date" required/>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="End Date"
                      label-for="endDate9">
          <b-form-input v-model="createContractEndDate" id="endDate9" class="marg-bot"
                        type="date" placeholder="End Date" required/>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="Pensum %"
                      label-for="pensum9">
          <b-form-input v-model="createContractPensum" type="number" id="pensum9"
                        min="1" max="100" class="marg-bot"
                        placeholder="Pensum" required/>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="Employee"
                      label-for="roles9">
          <b-form-select v-model="createContractEmployeeId" :options="employeeIdOptions"
                         class="marg-bot" id="roles9" required>
          </b-form-select>
        </b-form-group>
        <b-row class="marg-top">
          <b-col>
            <b-button variant="success" class="float-right" type="submit"
                      :disabled="validateState()">Create
            </b-button>
            <b-button @click="createContractModalCancel"
                      class="float-right marg-right">Cancel
            </b-button>
          </b-col>
        </b-row>
      </b-form>
    </b-modal>

    <!-- modal graph for contract -->
    <b-modal ref="modalGraph" id="modalGraph" size="xl"
             @hide="modalGraphCancel" hide-footer
             :title="`Contract ${this.selectedContractEmail}: ${this.selectedContractPensum}%`"
             centered>
      <CalendarContracts :contractId=this.graphId
                         :contractStart=this.selectedContractStartDate
                         :contractEnd=this.selectedContractEndDate
                         :contractPensum=this.selectedContractPensum
                         v-if="this.graphId">
      </CalendarContracts>
    </b-modal>
  </b-container>
</template>

<script>
import axios from 'axios';
import CalendarContracts from '@/components/CalendarContracts.vue';

const items = [];

export default {
  name: 'Contracts',
  components: {
    CalendarContracts,
  },
  props: {
    loggedInRole: String,
  },

  beforeMount() {
    if (localStorage.getItem('token') !== null) {
      this.restHeader = { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } };
      this.getContract();
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
          key: 'id',
          label: 'ID',
          sortable: true,
        },
        {
          key: 'startDate',
          label: 'StartDate',
          sortable: true,
        },
        {
          key: 'endDate',
          label: 'EndDate',
          sortable: true,
          sortDirection: 'desc',
        },
        {
          key: 'pensumPercentage',
          label: 'Pensum %',
          sortable: true,
          sortDirection: 'desc',
        },
        {
          key: 'email',
          label: 'Employee Email',
          sortable: true,
        },
        {
          key: 'graph',
          label: 'Graph',
        },
      ],
      currentPage: 1,
      perPage: 5,
      totalRows: items.length,
      pageOptions: [5, 10, 15],
      sortBy: null,
      sortDesc: false,
      sortDirection: 'asc',
      filter: null,
      modalInfo: {
        title: '',
        content: '',
      },
      filterToDate: null,
      filterFromDate: null,
      // Info Modal data
      selectedContractId: '',
      selectedContractStartDate: '',
      selectedContractEndDate: '',
      selectedContractPensum: null,
      selectedContractEmployeeId: '',
      selectedContractEmail: '',
      // Create Modal data
      createContractId: '',
      createContractStartDate: '',
      createContractEndDate: '',
      createContractPensum: null,
      createContractEmployeeId: '',
      createContractEmail: '',
      infoTitle: '',
      employeeIdOptions: [],
      // Graph Modal Data
      graphId: '',
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
    getContract() {
      axios.get(`${this.ApiServer}:${this.ApiPort}/api/contract`, this.restHeader)
        .then((response) => {
          this.totalRows = response.data.length;
          return response.data;
        })
        .then((its) => {
          this.combineItems(its);
        });
    },
    createContract(evt) {
      evt.preventDefault();
      const data = {
        startDate: this.createContractStartDate,
        endDate: this.createContractEndDate,
        pensumPercentage: this.createContractPensum,
        employeeId: this.createContractEmployeeId,
      };
      // eslint-disable-next-line
      Promise.resolve(this.getEmailFromEmployeeId(this.createContractEmployeeId))
      // eslint-disable-next-line
        .then(email => this.createContractEmail = email)
        .then(() => axios.post(`${this.ApiServer}:${this.ApiPort}/api/contract`, data, this.restHeader))
        // eslint-disable-next-line
        .then(async (response) => await {
          res: response,
          email: this.createContractEmail,
        })
        .then((response) => {
          const newContract = {
            id: response.res.data.id,
            startDate: response.res.data.startDate,
            endDate: response.res.data.endDate,
            pensumPercentage: response.res.data.pensumPercentage,
            employeeId: response.res.data.employeeId,
            email: response.email,
          };
          return newContract;
        })
        .then((newContract) => {
          this.items.unshift(newContract);
          this.totalRows = this.items.length;
          this.createContractModalCancel();
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    createContractModalCancel() {
      this.createContractStartDate = '';
      this.createContractEndDate = '';
      this.createContractPensum = '';
      this.createContractEmployeeId = null;
      this.$refs.createContract.hide();
    },
    infoContractModal(evt) {
      this.selectedContractId = evt.id;
      this.selectedContractStartDate = evt.startDate;
      this.selectedContractEndDate = evt.endDate;
      this.selectedContractPensum = evt.pensumPercentage;
      this.selectedContractEmployeeId = evt.employeeId;
      this.selectedContractEmail = evt.email;
      this.infoTitle = `Contract Info of ${evt.email}`;
      this.$refs.infoContractModal.show();
    },
    updateContract(evt) {
      evt.preventDefault();

      const data = {
        startDate: this.selectedContractStartDate,
        endDate: this.selectedContractEndDate,
        pensumPercentage: this.selectedContractPensum,
        employeeId: this.selectedContractEmployeeId,
      };

      axios.put(`${this.ApiServer}:${this.ApiPort}/api/contract/${this.selectedContractId}`, data, this.restHeader)
      // eslint-disable-next-line
        .then((response) => {
          for (let i = 0; i < this.items.length; i += 1) {
            if (this.items[i].id === this.selectedContractId) {
              this.items[i].startDate = this.selectedContractStartDate;
              this.items[i].endDate = this.selectedContractEndDate;
              this.items[i].pensumPercentage = this.selectedContractPensum;
              this.items[i].employeeId = this.selectedContractEmployeeId;
            }
          }

          this.infoContractCancel();
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    infoContractDelete() {
      axios.delete(`${this.ApiServer}:${this.ApiPort}/api/contract/${this.selectedContractId}`, this.restHeader)
      // eslint-disable-next-line
        .then((response) => {
          for (let i = 0; i < this.items.length; i += 1) {
            if (this.items[i].id === this.selectedContractId) {
              this.items.splice(i, 1);
            }
          }

          this.totalRows = this.items.length;
          this.infoContractCancel();
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    infoContractCancel() {
      this.selectedContractId = null;
      this.selectedContractStartDate = '';
      this.selectedContractEndDate = '';
      this.selectedContractPensum = null;
      this.selectedContractEmployeeId = null;

      this.$refs.infoContractModal.hide();
    },
    getEmployees() {
      const employees = [{ value: null, text: 'Employee', disabled: true }];
      if (this.loggedInRole === 'ADMINISTRATOR') {
        axios.get(`${this.ApiServer}:${this.ApiPort}/api/employee`, this.restHeader)
          .then(response => response.data)
          .then(res => res.forEach((entry) => {
            if (entry.role !== 'ADMINISTRATOR' && entry.active === true) {
              employees.push({ value: entry.id, text: entry.emailAddress });
            }
          }))
          .then(() => employees)
          .catch((error) => {
            this.errorHandler(error);
          });
      }
      this.employeeIdOptions = employees;
    },
    combineItems(its) {
      const employees = [];
      axios.get(`${this.ApiServer}:${this.ApiPort}/api/employee`, this.restHeader)
        .then(response => response.data)
        .then(res => res.forEach(entry => employees
          .push({ value: entry.id, text: entry.emailAddress })))
        .then(() => {
          // eslint-disable-next-line
          for (let i = 0; i < its.length; i++) {
            // eslint-disable-next-line
            for (let j = 0; j < employees.length; j++) {
              if (its[i].employeeId === employees[j].value) {
                if (employees[j].text !== undefined) {
                  // eslint-disable-next-line
                  its[i].email = employees[j].text;
                }
              }
            }
            // eslint-disable-next-line
            if (!its[i].email) {
              // eslint-disable-next-line
              its[i].email = 'n.a.';
            }
          }
        })
        .then(() => {
          this.items = its;
        });
    },
    async getEmailFromEmployeeId(employeeId) {
      let address;
      await axios.get(`${this.ApiServer}:${this.ApiPort}/api/employee/${employeeId}`, this.restHeader)
      // eslint-disable-next-line
        .then(response => address = response.data.emailAddress);
      return address;
    },
    validateState() {
      if (!this.createContractStartDate || !this.createContractEndDate) {
        return true;
      }
      if (this.createContractEndDate < this.createContractStartDate) {
        return true;
      }
      return false;
    },
    resetFilter() {
      this.filterFromDate = null;
      this.filterToDate = null;
      this.getContract();
    },
    applyFilter() {
      if (this.filterFromDate === null && this.filterToDate === null) {
        return;
      }

      let from = '';

      if (this.filterFromDate !== null && this.filterFromDate !== '') {
        from = `fromDate=${this.filterFromDate}&`;
      }

      let to = '';

      if (this.filterToDate !== null && this.filterFromDate !== '') {
        to = `toDate=${this.filterToDate}`;
      }

      axios.get(`${this.ApiServer}:${this.ApiPort}/api/contract?${from}${to}`, this.restHeader)
        .then((response) => {
          this.totalRows = response.data.length;
          this.combineItems(response.data);
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    modalGraph(item) {
      this.$refs.modalGraph.show();
      this.graphId = item.id;
      this.selectedContractEmail = item.email;
      this.selectedContractPensum = item.pensumPercentage;
      this.selectedContractStartDate = item.startDate;
      this.selectedContractEndDate = item.endDate;
    },
    modalGraphCancel() {
      this.graphId = '';
      this.selectedContractEmail = '';
      this.selectedContractPensum = null;
      this.selectedContractStartDate = '';
      this.selectedContractEndDate = '';
      this.$refs.modalGraph.hide();
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
h4 {
  margin-top: 15px;
}

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
