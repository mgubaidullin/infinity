const AggregationsTemplate = `
<div>
    <section class="pf-c-page__main-section pf-m-light">
        <div class="pf-l-split pf-m-gutter">
            <div class="pf-l-split__item">
                <div class="pf-c-content">
                    <h1>Aggregations</h1>
                </div>
            </div>
        </div>
    </section>
    <section class="pf-c-page__main-section">
        <table class="pf-c-table pf-m-grid-md" role="grid">
            <thead>
            <tr>
                <th>Group</th>
                <th>Type</th>
                <th>Horizon</th>
                <th>Period</th>
                <th>AVG</th>
                <th>MIN</th>
                <th>MAX</th>
                <th>MEAN</th>
                <th>SUM</th>
                <th>COUNT</th>
            </tr>
            </thead>
            <tr v-show="rows.length === 0" class="pf-m-height-auto" role="row">
                <td role="cell" colspan="8">
                    <div class="pf-l-bullseye">
                        <div class="pf-c-empty-state pf-m-sm">
                            <div v-show="showSpinner === false" class="pf-c-empty-state__content">
                                <i class="fas fa- fa-search pf-c-empty-state__icon" aria-hidden="true"></i>
                                <h2 class="pf-c-title pf-m-lg">No results found</h2>
                                <div class="pf-c-empty-state__body">No results match the select criteria.</div>
                            </div>
                            <div v-show="showSpinner === true" class="pf-c-empty-state__content">
                                <div class="pf-c-empty-state__icon">
                            <span class="pf-c-spinner" role="progressbar" aria-valuetext="Loading...">
                              <span class="pf-c-spinner__clipper"></span>
                              <span class="pf-c-spinner__lead-ball"></span>
                              <span class="pf-c-spinner__tail-ball"></span>
                            </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>
            <tr v-show="rows.length != 0" v-for="row in rows" :key="row.eventGroup + row.eventType + row.horizon + row.period">
                <td>{{row.eventGroup}}</td>
                <td>{{row.eventType}}</td>
                <td>{{row.horizon}}</td>
                <td>{{row.period}}</td>
                <td>{{row.avgValue}}</td>
                <td>{{row.minValue}}</td>
                <td>{{row.maxValue}}</td>
                <td>{{row.meanValue}}</td>
                <td>{{row.sumValue}}</td>
                <td>{{row.countValue}}</td>
                </td>
            </tr>
        </table>
    </section>
</div>
`

export { AggregationsTemplate }