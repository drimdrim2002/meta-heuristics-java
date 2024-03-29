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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import domain.CloudComputer;
import domain.CloudProcess;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import score.ScoreCalculator;

public class CloudProcessSwapMove extends AbstractMove implements Serializable {

    private CloudProcess leftCloudProcess;
    private CloudProcess rightCloudProcess;

    public CloudProcessSwapMove(CloudProcess leftCloudProcess, CloudProcess rightCloudProcess) {
        this.leftCloudProcess = leftCloudProcess;
        this.rightCloudProcess = rightCloudProcess;
    }

    @Override
    public boolean isMoveDoable(ScoreCalculator scoreCalculator) {
        return !Objects.equals(leftCloudProcess.getComputer(), rightCloudProcess.getComputer());
    }

    @Override
    public CloudProcessSwapMove doMove(ScoreCalculator scoreCalculator) {
        CloudProcessSwapMove undoMove = createUndoMove(scoreCalculator);
        doMoveOnGenuineVariables(scoreCalculator);
        return undoMove;
    }


    @Override
    protected CloudProcessSwapMove createUndoMove(ScoreCalculator scoreDirector) {
        return new CloudProcessSwapMove(rightCloudProcess, leftCloudProcess);
    }
    @Override
    protected void doMoveOnGenuineVariables(ScoreCalculator scoreDirector) {
        CloudComputer oldLeftCloudComputer = leftCloudProcess.getComputer();
        CloudComputer oldRightCloudComputer = rightCloudProcess.getComputer();
        scoreDirector.beforeVariableChanged(leftCloudProcess);
        leftCloudProcess.setComputer(oldRightCloudComputer);
        scoreDirector.afterVariableChanged(leftCloudProcess);
        scoreDirector.beforeVariableChanged(rightCloudProcess);
        rightCloudProcess.setComputer(oldLeftCloudComputer);
        scoreDirector.afterVariableChanged(rightCloudProcess);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof CloudProcessSwapMove) {
            CloudProcessSwapMove other = (CloudProcessSwapMove) o;
            return new EqualsBuilder()
                    .append(leftCloudProcess, other.leftCloudProcess)
                    .append(rightCloudProcess, other.rightCloudProcess)
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(leftCloudProcess)
                .append(rightCloudProcess)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Swap!! " +leftCloudProcess.getId()+ " {" + leftCloudProcess.getComputer().getId() +  "} <-> "
                + rightCloudProcess.getId() + " {" + rightCloudProcess.getComputer().getId() + "}";
    }

}
