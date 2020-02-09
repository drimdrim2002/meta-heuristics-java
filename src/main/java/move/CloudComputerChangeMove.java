/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package move;

import domain.CloudBalance;
import domain.CloudComputer;
import domain.CloudProcess;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import score.ScoreCalculator;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;


public class CloudComputerChangeMove  extends AbstractMove {

    private CloudProcess cloudProcess;
    private CloudComputer fromCloudComputer;
    private CloudComputer toCloudComputer;

    public CloudComputerChangeMove(CloudProcess cloudProcess,CloudComputer fromCloudComputer, CloudComputer toCloudComputer) {
        this.cloudProcess = cloudProcess;
        this.fromCloudComputer = fromCloudComputer;
        this.toCloudComputer = toCloudComputer;
    }

    public boolean isMoveDoable(ScoreCalculator scoreCalculator) {
        return !Objects.equals(fromCloudComputer, toCloudComputer);
    }

    public void undoMove(ScoreCalculator scoreCalculator) {
        scoreCalculator.beforeVariableChanged(cloudProcess);
        cloudProcess.setComputer(fromCloudComputer);
        scoreCalculator.afterVariableChanged(cloudProcess);
    }


    public void doMove(ScoreCalculator scoreCalculator) {
        scoreCalculator.beforeVariableChanged(cloudProcess);
        cloudProcess.setComputer(toCloudComputer);
        scoreCalculator.afterVariableChanged(cloudProcess);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof CloudComputerChangeMove) {
            CloudComputerChangeMove other = (CloudComputerChangeMove) o;
            return new EqualsBuilder()
                    .append(cloudProcess, other.cloudProcess)
                    .append(toCloudComputer, other.toCloudComputer)
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(cloudProcess)
                .append(toCloudComputer)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Change !! " + cloudProcess.getId() + " {" + cloudProcess.getComputer().getId() + " -> " + toCloudComputer.getId() + "}";
    }

}
