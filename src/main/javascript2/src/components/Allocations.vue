<template>
<b-container fluid>
  <div class="top-alert">
    <b-alert class="inner-alert" variant="danger" dismissible
    :show="showErrorAlert" @dismissed="dismissErrorAlert">
      {{this.errorMsg}}
    </b-alert>
  </div>

  <h1>Allocations</h1>
  <!-- Allocation Interface controls -->
  <h4>Data filter</h4>
  <b-row>
    <b-col>
      <b-form-group v-if="this.loggedInRole !== 'DEVELOPER'" label-cols="2" label="Employee">
        <b-form-select v-model="filterEmpId" :options="filterEmpOptions">
        </b-form-select>
      </b-form-group>
      <b-form-group label-cols="2" label="Project">
        <b-form-select v-model="filterProjectId" :options="filterProjectOptions">
        </b-form-select>
      </b-form-group>
    </b-col>
    <b-col>
      <b-form-group label-cols="2" label="From Date">
        <b-form-input type="date" v-bind:max="filterToDate" v-model="filterFromDate">
        </b-form-input>
      </b-form-group>
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
    id="allocationTable"
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
    @row-clicked="allocationInfoModal"
  >
  </b-table>

  <b-row>
    <b-col md="6" class="my-1">
      <b-pagination
        v-model="currentPage"
        :total-rows="totalRows"
        :per-page="perPage"
        class="my-0"
        aria-controls="allocationTable"
      />
    </b-col>
    <b-col md="6" v-if="this.loggedInRole !=='DEVELOPER'">
      <b-button
        class="float-right"
        variant="success"
        @click="createAllocModalOpen"
        v-b-modal.createAllocationModal>
        Create Allocation
      </b-button>
    </b-col>
  </b-row>

  <!-- Info modal -->
  <b-modal ref="infoAllocationModal" id="infoAllocationModal" title="Info Allocation"
    size="lg" @hide="infoAllocationCancel"
    hide-footer hide-header-close
    centered>
    <b-form @submit="updateAllocation">
      <b-form-group v-if="this.loggedInRole === 'ADMINISTRATOR'"
        label-cols="4" label-cols-lg="2" label="ID"
        label-for="selectedId2">
        <b-form-input id="selectedId2"
          v-model="selectedAllocationId"
          disabled required>
        </b-form-input>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label="Start Date"
        label-for="selectedStartDate2">
        <b-form-input id="selectedStartDate2"
          v-model="selectedAllocationStartDate" type="date"
          v-bind:max="selectedAllocationEndDate" required>
        </b-form-input>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label="End Date"
        label-for="selectedEndDate2">
        <b-form-input id="selectedEndDate2" type="date"
          v-model="selectedAllocationEndDate"
          v-bind:min="selectedAllocationStartDate" required>
        </b-form-input>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label="Contract Id"
        label-for="selectedContractId2">
        <b-form-input id="selectedContractId2"
          v-model="selectedAllocationContractId" disabled required>
        </b-form-input>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label="Project Id"
        label-for="selectedAllocationRole">
        <b-form-input id="selectedAllocationRole" v-model="selectedAllocationProjectId"
          class="marg-bot" disabled required>
        </b-form-input>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label="Pensum %"
        label-for="selectedAllocationPensumPercentage">
        <b-form-input id="selectedAllocationPensumPercentage"
          v-model="selectedAllocationPensumPercentage"
          class="marg-bot"
          type="number" min="0" max="100" required></b-form-input>
      </b-form-group>
      <b-row>
        <b-col>
          <b-button @click="infoAllocationDelete" variant="danger">
            Delete Allocation
          </b-button>
        </b-col>
        <b-col>
          <b-button
            variant="warning" class="float-right marg-left"
            type="submit">Update</b-button>
          <b-button @click="infoAllocationCancel" class="float-right">Cancel</b-button>
        </b-col>
      </b-row>
    </b-form>
  </b-modal>

  <!-- Info modal Read Only -->
  <b-modal ref="infoAllocationModalRO" id="infoAllocationModalRO" title="Info Allocation"
    size="lg" @hide="infoAllocationCancelRO"
    hide-footer hide-header-close centered>
    <b-form>
      <b-form-group label-cols="4" label-cols-lg="2" label="Start Date"
        label-for="selectedStartDate2RO">
        <b-form-input id="selectedStartDate2RO" type="date"
          v-model="selectedAllocationStartDate" disabled>
        </b-form-input>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label="End Date"
        label-for="selectedEndDate2RO">
        <b-form-input id="selectedEndDate2RO" type="date"
          v-model="selectedAllocationEndDate" disabled>
        </b-form-input>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label="Contract Id"
        label-for="selectedContractId2RO">
        <b-form-input id="selectedContractId2RO"
          v-model="selectedAllocationContractId" disabled>
        </b-form-input>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label="Project Id"
        label-for="selectedAllocationRoleRO">
        <b-form-input id="selectedAllocationRoleRO" v-model="selectedAllocationProjectId"
          class="marg-bot" disabled>
        </b-form-input>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label="Pensum %"
        label-for="selectedAllocationPensumPercentageRO">
        <b-form-input id="selectedAllocationPensumPercentageRO"
          v-model="selectedAllocationPensumPercentage"
          class="marg-bot" type="number" disabled>
          </b-form-input>
      </b-form-group>
      <b-row>
        <b-col>
          <b-button @click="infoAllocationCancelRO" class="float-right">Cancel</b-button>
        </b-col>
      </b-row>
    </b-form>
  </b-modal>

  <!-- Create Allocation Modal -->
  <b-modal ref="createAllocation" id="createAllocationModal"
    title="Create Allocation" @hide="createAllocationModalCancel" hide-footer
    hide-header-close size="lg" centered>
    <b-form @submit="createAllocation">
      <b-form-group label-cols="4" label-cols-lg="2" label="Start Date"
        label-for="startDate2">
        <b-form-input v-model="createAllocationStartDate" id="startDate2" class="marg-bot"
        type="date" placeholder="Start Date" v-bind:max="createAllocationEndDate" required/>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label="End Date"
        label-for="endDate2">
        <b-form-input v-model="createAllocationEndDate" id="endDate2" class="marg-bot"
          type="date" placeholder="End Date" v-bind:min="createAllocationStartDate" required/>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label="Contract ID"
        label-for="contractId12">
        <b-form-select v-model="createAllocationContractId" :options="contractIdOptions"
          class="marg-bot" id="contractId12" required>
        </b-form-select>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label="Project ID"
        label-for="projectId2">
        <b-form-select v-model="createAllocationProjectId" id="projectId2" class="marg-bot"
          :options="projectIdOptions" placeholder="Project" required>
        </b-form-select>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label="Pensum %"
        label-for="pensum2">
        <b-form-input v-model="createAllocationPensumPercentage" id="pensum2"
          class="marg-bot" placeholder="Pensum %"
          type="number" min="0" max="100" required/>
      </b-form-group>
      <b-row class="marg-top">
        <b-col>
          <b-button variant="success" class="float-right" type="submit">Create</b-button>
          <b-button @click="createAllocationModalCancel"
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
  name: 'Allocations',
  props: {
    loggedInRole: String,
    loggedInId: String,
  },
  beforeMount() {
    if (localStorage.getItem('token') !== null) {
      this.restHeader = { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } };
      this.getAllocation();
      this.loadFilterOptions();
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
          key: 'contractId',
          label: 'Contract Id',
          sortable: true,
        },
        {
          key: 'projectName',
          label: 'Project Name',
          sortable: true,
        },
        {
          key: 'startDate',
          label: 'Start Date',
          sortable: true,
          sortDirection: 'desc',
        },
        {
          key: 'endDate',
          label: 'End Date',
          sortable: true,
          sortDirection: 'desc',
        },
        {
          key: 'pensumPercentage',
          label: 'Pensum %',
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
      filterFromDate: null,
      filterToDate: null,
      filterEmpId: null,
      filterEmpOptions: [],
      filterProjectId: null,
      filterProjectOptions: [],
      // Create allocation data
      createAllocationStartDate: '',
      createAllocationEndDate: '',
      createAllocationContractId: '',
      createAllocationProjectId: '',
      createAllocationPensumPercentage: null,
      contractIdOptions: [],
      projectIdOptions: [],
      showFailCreateAllocationAlertDATE: false,
      // Info allocation data
      selectedAllocationId: '',
      selectedAllocationStartDate: '',
      selectedAllocationEndDate: '',
      selectedAllocationContractId: '',
      selectedAllocationProjectId: '',
      selectedAllocationPensumPercentage: null,
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
    loadFilterOptions() {
      axios.get(`${this.ApiServer}:${this.ApiPort}/api/project`, this.restHeader)
        .then((response) => {
          this.filterProjectOptions.push({ value: null, text: 'Project', disabled: true });
          for (let i = 0; i < response.data.length; i += 1) {
            this.filterProjectOptions.push({
              value: response.data[i].id,
              text: response.data[i].name,
            });
          }
        })
        .catch((error) => {
          this.errorHandler(error);
        });
      if (this.loggedInRole !== 'DEVELOPER') {
        axios.get(`${this.ApiServer}:${this.ApiPort}/api/employee`, this.restHeader)
          .then((response) => {
            this.filterEmpOptions.push({ value: null, text: 'Employee', disabled: true });

            for (let i = 0; i < response.data.length; i += 1) {
              this.filterEmpOptions.push({
                value: response.data[i].id,
                text: response.data[i].emailAddress,
              });
            }
          })
          .catch((error) => {
            this.errorHandler(error);
          });
      }
    },
    applyFilter() {
      if (this.filterFromDate === null && this.filterToDate === null
        && this.filterEmpId === null && this.filterProjectId === null) {
        return;
      }

      if (this.loggedInRole === 'DEVELOPER' && this.filterFromDate === null
        && this.filterToDate === null && this.filterProjectId === null) {
        return;
      }

      let from = '';

      if (this.filterFromDate !== null && this.filterFromDate !== '') {
        from = `fromDate=${this.filterFromDate}&`;
      }

      let to = '';

      if (this.filterToDate !== null && this.filterToDate !== '') {
        to = `toDate=${this.filterToDate}&`;
      }

      let proj = '';

      if (this.filterProjectId !== null) {
        proj = `projectId=${this.filterProjectId}&`;
      }

      let emp = '';

      if (this.filterEmpId !== null) {
        emp = `employeeId=${this.filterEmpId}&`;
      }

      let allocs = [];
      let projs = [];
      const url = `${this.ApiServer}:${this.ApiPort}/api/allocation?${from}${to}${proj}${emp}`;
      axios.get(url,
        this.restHeader)
        .then((response) => {
          allocs = response.data;
        })
        .then(() => {
          axios.get(`${this.ApiServer}:${this.ApiPort}/api/project`, this.restHeader)
            .then((response) => {
              projs = response.data;

              for (let i = 0; i < allocs.length; i += 1) {
                allocs[i].projectEmail = 'n.a.';
                allocs[i].projectName = 'n.a.';
                allocs[i].projectPmId = null;
                for (let j = 0; j < projs.length; j += 1) {
                  // eslint-disable-next-line
                  if (allocs[i].projectId == projs[j].id) {
                    allocs[i].projectName = projs[j].name;
                    allocs[i].projectPmId = projs[j].projectManagerId;
                    break;
                  }
                }
              }

              this.items = allocs;
              this.totalRows = this.items.length;
            })
            .catch((error) => {
              this.errorHandler(error);
            });
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    createAllocModalOpen() {
      this.getAllocationIdOptions();
      this.getContractIdOptions();
    },
    resetFilter() {
      this.filterFromDate = null;
      this.filterToDate = null;
      this.filterEmpId = null;
      this.filterProjectId = null;

      this.getAllocation();
    },
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
    getAllocation() {
      let allocations = [];
      let projects = [];

      axios.get(`${this.ApiServer}:${this.ApiPort}/api/allocation`, this.restHeader)
        .then((response) => {
          allocations = response.data;
          // this.totalRows = this.items.length;
        })
        .then(() => {
          axios.get(`${this.ApiServer}:${this.ApiPort}/api/project`, this.restHeader)
            .then((response) => {
              projects = response.data;
            })
            .then(() => {
              for (let i = 0; i < allocations.length; i += 1) {
                allocations[i].projectEmail = 'n.a.';
                allocations[i].projectName = 'n.a.';
                allocations[i].projectPmId = null;
                for (let j = 0; j < projects.length; j += 1) {
                  // eslint-disable-next-line
                  if (allocations[i].projectId == projects[j].id) {
                    allocations[i].projectName = projects[j].name;
                    allocations[i].projectPmId = projects[j].projectManagerId;
                    break;
                  }
                }
              }
              this.items = allocations;
              this.totalRows = this.items.length;
            })
            .catch((error) => {
              this.errorHandler(error);
            });
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    createAllocation(evt) {
      evt.preventDefault();
      const data = {
        startDate: this.createAllocationStartDate,
        endDate: this.createAllocationEndDate,
        contractId: this.createAllocationContractId,
        projectId: this.createAllocationProjectId,
        pensumPercentage: this.createAllocationPensumPercentage,
      };

      const newAllocation = {};

      axios.post(`${this.ApiServer}:${this.ApiPort}/api/allocation`, data, this.restHeader)
        .then((response) => {
          newAllocation.id = response.data.id;
          newAllocation.startDate = response.data.startDate;
          newAllocation.endDate = response.data.endDate;
          newAllocation.contractId = response.data.contractId;
          newAllocation.projectId = response.data.projectId;
          newAllocation.pensumPercentage = response.data.pensumPercentage;

          axios.get(`${this.ApiServer}:${this.ApiPort}/api/project/${response.data.projectId}`, this.restHeader)
            .then((response2) => {
              newAllocation.projectName = response2.data.name;
              newAllocation.projectPmId = response2.data.projectManagerId;

              this.items.unshift(newAllocation);
              this.totalRows = this.items.length;

              this.createAllocationModalCancel();
            })
            .catch((error) => {
              this.errorHandler(error);
            });
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    createAllocationModalCancel() {
      this.createAllocationStartDate = '';
      this.createAllocationEndDate = '';
      this.createAllocationContractId = null;
      this.createAllocationProjectId = null;
      this.createAllocationPensumPercentage = null;
      // this.showFailCreateAllocationAlertDATE = false;
      this.$refs.createAllocation.hide();
    },
    allocationInfoModal(evt) {
      this.selectedAllocationId = evt.id;
      this.selectedAllocationStartDate = evt.startDate;
      this.selectedAllocationEndDate = evt.endDate;
      this.selectedAllocationContractId = evt.contractId;
      this.selectedAllocationProjectId = evt.projectId;
      this.selectedAllocationPensumPercentage = evt.pensumPercentage;

      if (this.loggedInRole === 'ADMINISTRATOR' || this.loggedInId === evt.projectPmId) {
        this.$refs.infoAllocationModal.show();
      } else {
        this.$refs.infoAllocationModalRO.show();
      }
    },
    updateAllocation(evt) {
      evt.preventDefault();

      const data = {
        startDate: this.selectedAllocationStartDate,
        endDate: this.selectedAllocationEndDate,
        contractId: this.selectedAllocationContractId,
        projectId: this.selectedAllocationProjectId,
        pensumPercentage: this.selectedAllocationPensumPercentage,
      };

      axios.put(`${this.ApiServer}:${this.ApiPort}/api/allocation/${this.selectedAllocationId}`, data, this.restHeader)
      // eslint-disable-next-line
        .then((response) => {
          for (let i = 0; i < this.items.length; i += 1) {
            if (this.items[i].id === this.selectedAllocationId) {
              this.items[i].startDate = this.selectedAllocationStartDate;
              this.items[i].endDate = this.selectedAllocationEndDate;
              this.items[i].contractId = this.selectedAllocationContractId;
              this.items[i].projectId = this.selectedAllocationProjectId;
              this.items[i].pensumPercentage = this.selectedAllocationPensumPercentage;
            }
          }

          this.infoAllocationCancel();
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    infoAllocationDelete() {
      axios.delete(`${this.ApiServer}:${this.ApiPort}/api/allocation/${this.selectedAllocationId}`, this.restHeader)
      // eslint-disable-next-line
        .then((response) => {
          for (let i = 0; i < this.items.length; i += 1) {
            if (this.items[i].id === this.selectedAllocationId) {
              this.items.splice(i, 1);
            }
          }

          this.totalRows = this.items.length;
          this.infoAllocationCancel();
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    infoAllocationCancel() {
      this.selectedAllocationId = null;
      this.selectedAllocationStartDate = '';
      this.selectedAllocationEndDate = '';
      this.selectedAllocationContractId = null;
      // this.selectedAllocationRole = null;

      this.$refs.infoAllocationModal.hide();
    },
    infoAllocationCancelRO() {
      this.selectedAllocationId = null;
      this.selectedAllocationStartDate = '';
      this.selectedAllocationEndDate = '';
      this.selectedAllocationContractId = null;
      // this.selectedAllocationRole = null;

      this.$refs.infoAllocationModalRO.hide();
    },
    getContractIdOptions() {
      // eslint-disable-next-line
      const listIds = [{ value: null, text: 'Contracts', disabled: true }];
      axios.get(`${process.env.VUE_APP_API_SERVER}:${process.env.VUE_APP_API_PORT}/api/contract`, this.restHeader)
        .then(response => response.data)
        .then((data) => {
          data.forEach((entry) => {
            listIds
              .push(
                {
                  disabled: false,
                  text: String(entry.id),
                  value: String(entry.id),
                },
              );
          });
        })
        .catch((error) => {
          this.errorHandler(error);
        });
      this.contractIdOptions = listIds;
    },
    getAllocationIdOptions() {
      const list = [{ value: null, text: 'Projects', disabled: true }];
      axios.get(`${process.env.VUE_APP_API_SERVER}:${process.env.VUE_APP_API_PORT}/api/project`, this.restHeader)
        .then(response => response.data)
        .then((data) => {
          data.forEach((entry) => {
            if (this.loggedInRole === 'ADMINISTRATOR' || (this.loggedInRole === 'PROJECTMANAGER' && this.loggedInId === entry.projectManagerId)) {
              list.push({
                text: entry.name,
                value: entry.id,
              });
            }
          });
        })
        .catch((error) => {
          this.errorHandler(error);
        });
      this.projectIdOptions = list;
    },
    getNameOfNewProject(projectId) {
      return axios.get(`${this.ApiServer}:${this.ApiPort}/api/project/${projectId}`, this.restHeader)
        .then(response => response.data);
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
