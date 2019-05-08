<template>
  <b-container fluid>
    <!-- Error Alert -->
    <div class="top-alert">
      <b-alert class="inner-alert" variant="danger" dismissible
      :show="showErrorAlert" @dismissed="dismissErrorAlert">
        {{this.errorMsg}}
      </b-alert>
    </div>

    <!-- Visible Content on page -->
    <h1>Projects</h1>
    <h4>Data filter</h4>
    <b-row>
      <b-col md="3">
        <b-form-group label-cols-sm="3" label="From Date" class="mb-0">
          <b-form-input type="date" v-bind:max="filterToDate"
                        v-model="filterFromDate">
          </b-form-input>
        </b-form-group>
      </b-col>
      <b-col md="3">
        <b-form-group label-cols-sm="3" label="To Date" class="mb-0">
          <b-form-input type="date" v-bind:min="filterFromDate"
                        v-model="filterToDate">
          </b-form-input>
        </b-form-group>
      </b-col>
      <b-col md="3">
        <b-form-group label-cols-sm="3" label="PM E-Mail" class="mb-0">
          <b-form-select v-model="filterPmId" :options="pmOptions">
          </b-form-select>
        </b-form-group>
      </b-col>
      <b-col md="3">
        <b-button variant="primary" class="float-right"
                  @click="applyFilter">Filter
        </b-button>
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
      @row-clicked="projectInfoModal"
    >
      <template slot="graph" slot-scope="row">
        <b-button size="sm" @click="modalGraph(row.item)" class="mr-1"
                  variant="primary">
          Project Calendar
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
          @click="createProjectModalOpen"
          v-b-modal.createProjectModal>
          Create Project
        </b-button>
      </b-col>
    </b-row>

    <!-- Info Project Modal -->
    <b-modal ref="infoProjectModal" id="infoProjectModal" title="Info Project"
             size="lg" @hide="infoProjectCancel" hide-footer hide-header-close centered>
      <b-form @submit="updateProject">
        <b-form-group v-if="this.loggedInRole === 'ADMINISTRATOR'"
                      label-cols="4" label-cols-lg="2" label="ID"
                      label-for="selectedProjectId">
          <b-form-input id="selectedProjectId"
                        v-model="selectedProjectId" disabled required/>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="Project Name"
                      label-for="selectedProjectName">
          <b-form-input id="selectedProjectName"
                        v-model="selectedProjectName"
                        v-bind:disabled="this.loggedInRole !== 'ADMINISTRATOR' &&
             this.loggedInId !== String(this.selectedProjectPmId)" required>
          </b-form-input>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="FTE"
                      label-for="selectedProjectFte">
          <b-form-input
            id="selectedProjectFte" v-model="selectedProjectFte"
            type="number" min="0"
            v-bind:disabled="this.loggedInRole !== 'ADMINISTRATOR' &&
             this.loggedInId !== String(this.selectedProjectPmId)" required>
          </b-form-input>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="Start Date"
                      label-for="selectedProjectStart">
          <b-form-input id="selectedProjectStart" v-model="selectedProjectStart"
                        type="date" disabled required>
          </b-form-input>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="End Date"
                      label-for="selectedProjectEnd">
          <b-form-input v-if="new Date().toISOString().split('T')[0] >
            this.selectedProjectStart"
                        id="selectedProjectEnd" v-model="selectedProjectEnd"
                        type="date" v-bind:min="new Date().toISOString().split('T')[0]"
                        v-bind:disabled="this.loggedInRole !== 'ADMINISTRATOR' &&
              this.loggedInId !== String(this.selectedProjectPmId)" required>
          </b-form-input>
          <b-form-input v-else id="selectedProjectEnd"
                        v-model="selectedProjectEnd" type="date"
                        v-bind:min="this.selectedProjectStart"
                        v-bind:disabled="this.loggedInRole !== 'ADMINISTRATOR' &&
            this.loggedInId !== String(this.selectedProjectPmId)" required>
          </b-form-input>
        </b-form-group>
        <b-form-group v-if="this.loggedInRole === 'ADMINISTRATOR'"
                      label-cols="4" label-cols-lg="2" label="PM ID"
                      label-for="selectedProjectPmId">
          <b-form-input id="selectedProjectPmId" v-model="selectedProjectPmId"
                        disabled required>
          </b-form-input>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="PM E-Mail"
                      label-for="selectedProjectPmMail">
          <b-form-select
            id="selectedProjectPmMail" v-model="selectedProjectPmIdMail"
            :options="pmOptions" v-bind:disabled="this.loggedInRole !== 'ADMINISTRATOR'"
            required>
          </b-form-select>
        </b-form-group>
        <b-row>
          <b-col>
            <b-button v-if="this.loggedInRole === 'ADMINISTRATOR'"
                      @click="infoProjectDelete" variant="danger">Delete Project
            </b-button>
          </b-col>
          <b-col>
            <b-button v-if="this.loggedInRole === 'ADMINISTRATOR' ||
              this.loggedInId === String(this.selectedProjectPmId)"
                      variant="warning" class="float-right marg-left"
                      type="submit">update
            </b-button>
            <b-button @click="infoProjectCancelBtn" class="float-right">Cancel</b-button>
          </b-col>
        </b-row>
      </b-form>
    </b-modal>

    <!-- Info Project Modal Read-Only -->
    <b-modal ref="infoProjectModalRO" id="infoProjectModalRO" title="Info Project"
             size="lg" @hide="infoProjectCancel" hide-footer hide-header-close centered>
      <b-form>
        <b-form-group v-if="this.loggedInRole === 'ADMINISTRATOR'"
                      label-cols="4" label-cols-lg="2" label="ID"
                      label-for="selectedProjectIdRO">
          <b-form-input id="selectedProjectIdRO"
                        v-model="selectedProjectId" disabled required/>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="Project Name"
                      label-for="selectedProjectNameRO">
          <b-form-input id="selectedProjectNameRO"
                        v-model="selectedProjectName" disabled required>
          </b-form-input>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="FTE Percentage"
                      label-for="selectedProjectFteRO">
          <b-form-input id="selectedProjectFteRO"
                        v-model="selectedProjectFte" disabled required>
          </b-form-input>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="Start Date"
                      label-for="selectedProjectStartRO">
          <b-form-input id="selectedProjectStartRO" v-model="selectedProjectStart"
                        type="date" disabled required>
          </b-form-input>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="End Date"
                      label-for="selectedProjectEndRO">
          <b-form-input id="selectedProjectEndRO" v-model="selectedProjectEnd"
                        type="date" disabled required>
          </b-form-input>
        </b-form-group>
        <b-form-group v-if="this.loggedInRole === 'ADMINISTRATOR'"
                      label-cols="4" label-cols-lg="2" label="PM ID"
                      label-for="selectedProjectPmIdRO">
          <b-form-input id="selectedProjectPmIdRO" v-model="selectedProjectPmId"
                        disabled required>
          </b-form-input>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="PM E-Mail"
                      label-for="selectedProjectPmMailRO">
          <b-form-select id="selectedProjectPmMailRO" v-model="selectedProjectPmIdMail"
                         :options="pmOptions" disabled required>
          </b-form-select>
        </b-form-group>
        <b-row>
          <b-col>
            <b-button @click="infoProjectROCancelBtn" class="float-right">Cancel</b-button>
          </b-col>
        </b-row>
      </b-form>
    </b-modal>

    <!-- Create Project Modal -->
    <b-modal ref="createProjectModal" id="createProjectModal"
             title="Create Project" @hide="createProjectModalCancel" hide-footer
             hide-header-close size="lg" centered>
      <b-form @submit="createProject">
        <b-form-group label-cols="4" label-cols-lg="2" label="Project Name"
                      label-for="projectName">
          <b-form-input id="projectName" class="marg-bot" v-model="createProjectName"
                        placeholder="Project Name" required/>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="FTE"
                      label-for="projectFte">
          <b-form-input id="projectFte" class="marg-bot" type="number"
                      v-model="createProjectFte" min="0"
                      placeholder="Full Time Employee" required/>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="Project Start"
                      label-for="projectStart">
          <b-form-input id="projectStart" class="marg-bot" type="date"
                        v-model="createProjectStart" v-bind:max="createProjectEnd"
                        placeholder="Project Start Date" required/>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="Project End"
                      label-for="projectEnd">
          <b-form-input id="projectEnd" class="marg-bot" type="date"
                        v-model="createProjectEnd" v-bind:min="createProjectStart"
                        placeholder="Project End Date" required/>
        </b-form-group>
        <b-form-group label-cols="4" label-cols-lg="2" label="PM E-Mail"
                      label-for="pmMailSelect">
          <b-form-select id="pmMailSelect" v-model="createProjectPmId"
                         :options="pmOptions" class="marg-bot" required>
          </b-form-select>
        </b-form-group>
        <b-row class="marg-top">
          <b-col>
            <b-button variant="success" class="float-right" type="submit">Create</b-button>
            <b-button @click="createProjectModalCancelBtn"
                      class="float-right marg-right">Cancel
            </b-button>
          </b-col>
        </b-row>
      </b-form>
    </b-modal>

    <!-- modal graph for project -->
    <b-modal ref="modalGraph" id="modalGraph" size="xl"
             @hide="modalGraphCancel" hide-footer
             :title="`Project ${this.selectedProjectName}`" centered>
      <Calendar :projectId=this.graphId v-if="this.graphId"></Calendar>
    </b-modal>
  </b-container>
</template>

<script>
import axios from 'axios';
import Calendar from '@/components/Calendar.vue';

const items = [];

export default {
  name: 'Projects',
  components: {
    Calendar,
  },
  props: {
    loggedInRole: String,
    loggedInId: String,
  },

  beforeMount() {
    if (localStorage.getItem('token') !== null) {
      this.restHeader = { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } };

      this.getProjects();
      this.loadPMs();

      if (this.loggedInRole === 'DEVELOPER') {
        this.fields.splice(5, 1);
      }
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
          key: 'name',
          label: 'Name',
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
          key: 'ftePercentage',
          label: 'FTE',
          sortable: true,
          sortDirection: 'desc',
        },
        {
          key: 'projectManagerMail',
          label: 'Projectmanager E-Mail',
          sortable: true,
          sortDirection: 'desc',
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
      filterFromDate: null,
      filterToDate: null,
      filterPmId: null,
      // Create project data
      createProjectName: '',
      createProjectFte: null,
      createProjectStart: '',
      createProjectEnd: '',
      createProjectPmId: null,
      pmOptions: [
        { value: null, text: 'PM', disabled: true },
      ],
      // Info project data
      selectedProjectId: '',
      selectedProjectName: '',
      selectedProjectFte: null,
      selectedProjectStart: '',
      selectedProjectEnd: '',
      selectedProjectPmId: '',
      selectedProjectPmIdMail: '',
      // Graph Project Id
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
    onFiltered(filteredItems) {
      // Trigger pagination to update the number of buttons/pages due to filtering
      this.totalRows = filteredItems.length;
      this.currentPage = 1;
    },
    resetFilter() {
      this.filterFromDate = null;
      this.filterToDate = null;
      this.filterPmId = null;
      this.getProjects();
    },
    applyFilter() {
      if (this.filterFromDate === null && this.filterToDate === null && this.filterPmId === null) {
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

      let pmId = '';

      if (this.filterPmId !== null) {
        pmId = `projectManagerId=${this.filterPmId.split('_')[0]}&`;
      }

      let PMs = [];
      let projects = [];
      const url = `${this.ApiServer}:${this.ApiPort}/api/project?${from}${to}${pmId}`;
      axios.get(url.substr(0, url.length - 1),
        this.restHeader)
        .then((response) => {
          projects = response.data;
        })
        .then(() => {
          axios.get(`${this.ApiServer}:${this.ApiPort}/api/employee?role=PROJECTMANAGER`, this.restHeader)
            .then((response) => {
              PMs = response.data;
            })
            .then(() => {
              for (let i = 0; i < projects.length; i += 1) {
                projects[i].projectManagerMail = 'n.a.';
                for (let j = 0; j < PMs.length; j += 1) {
                  if (projects[i].projectManagerId === PMs[j].id) {
                    projects[i].projectManagerMail = PMs[j].emailAddress;
                    break;
                  }
                }
              }

              this.items = projects;
            })
            .catch((error) => {
              this.errorHandler(error);
            });
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    createProject(evt) {
      evt.preventDefault();
      const pmId = this.createProjectPmId.substr(0, this.createProjectPmId.indexOf('_'));
      const pmMail = this.createProjectPmId.substr(this.createProjectPmId.indexOf('_') + 1);

      const data = {
        name: this.createProjectName,
        ftePercentage: this.createProjectFte,
        startDate: this.createProjectStart,
        endDate: this.createProjectEnd,
        projectManagerId: pmId,
      };

      axios.post(`${this.ApiServer}:${this.ApiPort}/api/project`, data, this.restHeader)
        .then((response) => {
          const newProject = {
            id: response.data.id,
            name: response.data.name,
            ftePercentage: response.data.ftePercentage,
            startDate: response.data.startDate,
            endDate: response.data.endDate,
            projectManagerId: response.data.projectManagerId,
            projectManagerMail: pmMail,
          };

          this.items.unshift(newProject);
          this.totalRows = this.items.length;

          this.createProjectModalCancelBtn();
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    createProjectModalCancelBtn() {
      this.$refs.createProjectModal.hide();
    },
    createProjectModalCancel() {
      this.createProjectName = '';
      this.createProjectFte = null;
      this.createProjectStart = '';
      this.createProjectEnd = '';
      this.createProjectPmId = null;
    },
    createProjectModalOpen() {
      this.loadPMs();
    },
    loadPMs() {
      axios.get(`${this.ApiServer}:${this.ApiPort}/api/employee?role=PROJECTMANAGER`, this.restHeader)
        .then((response) => {
          const arr = [{ value: null, text: 'PM', disabled: true }];
          for (let i = 0; i < response.data.length; i += 1) {
            arr.push({
              value: `${response.data[i].id}_${response.data[i].emailAddress}`,
              text: response.data[i].emailAddress,
            });
          }

          this.pmOptions = arr;
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    getProjects() {
      let PMs = [];
      let projects = [];

      axios.get(`${this.ApiServer}:${this.ApiPort}/api/project`, this.restHeader)
        .then((response) => {
          projects = response.data;
        })
        .then(() => {
          axios.get(`${this.ApiServer}:${this.ApiPort}/api/employee?role=PROJECTMANAGER`, this.restHeader)
            .then((response) => {
              PMs = response.data;
            })
            .then(() => {
              for (let i = 0; i < projects.length; i += 1) {
                projects[i].projectManagerMail = 'n.a.';
                for (let j = 0; j < PMs.length; j += 1) {
                  if (projects[i].projectManagerId === PMs[j].id) {
                    projects[i].projectManagerMail = PMs[j].emailAddress;
                    break;
                  }
                }
              }

              this.items = projects;
            })
            .catch((error) => {
              this.errorHandler(error);
            });
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    updateProject(evt) {
      evt.preventDefault();

      const pmId = this.selectedProjectPmIdMail.substr(0, this.selectedProjectPmIdMail.indexOf('_'));
      const pmMail = this.selectedProjectPmIdMail.substr(this.selectedProjectPmIdMail.indexOf('_') + 1);

      const data = {
        name: this.selectedProjectName,
        ftePercentage: this.selectedProjectFte,
        startDate: this.selectedProjectStart,
        endDate: this.selectedProjectEnd,
        projectManagerId: pmId,
      };

      axios.put(`${this.ApiServer}:${this.ApiPort}/api/project/${this.selectedProjectId}`, data, this.restHeader)
      // eslint-disable-next-line
        .then((response) => {
          for (let i = 0; i < this.items.length; i += 1) {
            if (this.items[i].id === this.selectedProjectId) {
              this.items[i].name = this.selectedProjectName;
              this.items[i].ftePercentage = this.selectedProjectFte;
              this.items[i].endDate = this.selectedProjectEnd;
              this.items[i].projectManagerId = this.selectedProjectPmId;
              this.items[i].projectManagerMail = pmMail;
            }
          }

          this.infoProjectCancelBtn();
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    projectInfoModal(evt) {
      this.selectedProjectId = evt.id;
      this.selectedProjectName = evt.name;
      this.selectedProjectFte = evt.ftePercentage;
      this.selectedProjectStart = evt.startDate;
      this.selectedProjectEnd = evt.endDate;
      this.selectedProjectPmId = String(evt.projectManagerId);
      this.selectedProjectPmIdMail = `${evt.projectManagerId}_${evt.projectManagerMail}`;

      this.loadPMs();

      if (evt.endDate >= new Date().toISOString().split('T')[0]) {
        this.$refs.infoProjectModal.show();
      } else {
        this.$refs.infoProjectModalRO.show();
      }
    },
    infoProjectCancelBtn() {
      this.$refs.infoProjectModal.hide();
    },
    infoProjectROCancelBtn() {
      this.$refs.infoProjectModalRO.hide();
    },
    infoProjectCancel() {
      this.selectedProjectId = null;
      this.selectedProjectName = '';
      this.selectedProjectFte = null;
      this.selectedProjectStart = '';
      this.selectedProjectEnd = '';
      this.selectedProjectPmId = null;
    },
    infoProjectDelete() {
      axios.delete(`${this.ApiServer}:${this.ApiPort}/api/project/${this.selectedProjectId}`, this.restHeader)
      // eslint-disable-next-line
        .then((response) => {
          for (let i = 0; i < this.items.length; i += 1) {
            if (this.items[i].id === this.selectedProjectId) {
              this.items.splice(i, 1);
              break;
            }
          }
          this.totalRows = this.items.length;
          this.infoProjectCancelBtn();
        })
        .catch((error) => {
          this.errorHandler(error);
        });
    },
    modalGraph(item) {
      this.$refs.modalGraph.show();
      this.graphId = item.id;
      this.selectedProjectName = item.name;
    },
    modalGraphCancel() {
      this.graphId = '';
      this.selectedProjectName = '';
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
