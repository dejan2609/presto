/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.sql.planner.iterative.rule;

import com.facebook.presto.sql.planner.PlanNodeIdAllocator;
import com.facebook.presto.sql.planner.Symbol;
import com.facebook.presto.sql.planner.plan.PlanNode;
import com.facebook.presto.sql.planner.plan.TableScanNode;

import java.util.Optional;
import java.util.Set;

import static com.facebook.presto.util.MoreLists.filteredCopy;
import static com.google.common.collect.Maps.filterKeys;

public class PruneTableScanColumns
        extends ProjectOffPushDownRule<TableScanNode>
{
    public PruneTableScanColumns()
    {
        super(TableScanNode.class);
    }

    @Override
    protected Optional<PlanNode> pushDownProjectOff(PlanNodeIdAllocator idAllocator, TableScanNode tableScanNode, Set<Symbol> referencedOutputs)
    {
        return Optional.of(
                new TableScanNode(
                        tableScanNode.getId(),
                        tableScanNode.getTable(),
                        filteredCopy(tableScanNode.getOutputSymbols(), referencedOutputs::contains),
                        filterKeys(tableScanNode.getAssignments(), referencedOutputs::contains),
                        tableScanNode.getLayout(),
                        tableScanNode.getCurrentConstraint(),
                        tableScanNode.getOriginalConstraint()));
    }
}
