import random
import re


def createKnowledgeBases(variables):
    knowledge_base1 = []
    knowledge_base2 = []
    for i in range(variables + 1):
        knowledge_base1.append('IntVar,Var%d,' % i)
        knowledge_base2.append('IntVar,Var%d,' % i)
        #
        values = random.randrange(1, 4)
        for j in range(values + 1):
            if j < values:
                value = 'Val%d%d,' % (i, j)
                knowledge_base1[i] = knowledge_base1[i] + value
            else:
                value = 'Val%d%d\n' % (i, j)
                knowledge_base1[i] = knowledge_base1[i] + value
        #
        values = random.randrange(1, 4)
        for j in range(values + 1):
            if j < values:
                value = 'Val%d%d,' % (i, j)
                knowledge_base2[i] = knowledge_base2[i] + value
            else:
                value = 'Val%d%d\n' % (i, j)
                knowledge_base2[i] = knowledge_base2[i] + value

    #
    with open('knowledge_base1.txt', 'w') as file1:
        file1.writelines(knowledge_base1)
    with open('knowledge_base2.txt', 'w') as file2:
        file2.writelines(knowledge_base2)
    #
    return knowledge_base1, knowledge_base2


def createConstraints(knowledge_base, kb_no, operator_list):
    constraints = []
    con_variables = []
    save_operators = []
    operator_count = 0;
    for i in range(len(knowledge_base)):
        variables = []
        variable = knowledge_base[i].split(",")
        for j in range(len(variable)):
            if j <= 1 or variable[j] == '\n':
                continue
            else:
                var = variable[1].strip() + ', ' + variable[j]
                var = var.translate({ord('}'): None})
                var = var.replace('\n', '')
                var = var.translate({ord('\n'): None})
                variables.append(var)
        con_variables.append(variables)
    #
    operators1 = ['others,', 'independent,']
    operators2 = ['imply,', 'alternative,']
    probabilities1 = [0.5, 0.5]
    probabilities2 = [0.5, 0.5]
    for i in range(len(con_variables) - 1):
        for j in range(len(con_variables[i])):
            if len(operator_list) == 0:
                operator1 = random.choices(operators1, probabilities1)
                save_operator = [con_variables[i][j], operator1]
                save_operators.append(save_operator)
            else:
                if operator_count < len(operator_list) and con_variables[i][j] == operator_list[operator_count][0]:
                    operator1 = operator_list[operator_count][1]
                    if j == len(con_variables[i]) - 1 and (operator_count + 1) < len(operator_list) and \
                            operator_list[operator_count][0].split(",")[0] == operator_list[operator_count + 1][0].split(",")[0]:
                        while operator_list[operator_count][0].split(",")[0] == operator_list[operator_count + 1][0].split(",")[0]:
                            if len(operator_list) > (operator_count + 1):
                                operator_count += 1
                                if len(operator_list) - 1 == (operator_count):
                                    break
                        operator_count += 1
                    else:
                        operator_count += 1
                else:
                    operator1 = ['others']
            if j == 0 and operator1[0] == 'independent,':
                for k in range(i + 1, len(con_variables)):
                    constraints.append((re.sub(",.*$", "", con_variables[i][j]) + ',' + operator1[0] +
                                        re.sub(",.*$", "", con_variables[k][0]) + '\n').replace(" ", ""))
                if len(operator_list) == 0:
                    for k in range(j + 1, len(con_variables[i])):
                        save_operator = [con_variables[i][k], operator1]
                        save_operators.append(save_operator)
                else:
                    if j < len(con_variables[i]) - 1 and (operator_count + 1) < len(operator_list) and \
                            operator_list[operator_count][0].split(",")[0] == operator_list[operator_count + 1][0].split(",")[0]:
                        while operator_list[operator_count][0].split(",")[0] == operator_list[operator_count + 1][0].split(",")[0]:
                            if len(operator_list) > (operator_count + 1):
                                operator_count += 1
                                if len(operator_list) - 1 == (operator_count):
                                    break
                        operator_count += 1
                    else:
                        operator_count += 1
                break
            else:
                for k in range(i + 1, len(con_variables)):
                    operator2 = random.choices(operators2, probabilities2)
                    if operator2[0] == 'imply,':
                        value_pos = random.randrange(len(con_variables[k]))
                        constraints.append(
                            (con_variables[i][j] + ',' + operator2[0] + con_variables[k][value_pos] + '\n').replace(" ", ""))
                    else:
                        if len(con_variables[k]) <= 3:
                            num_value = 2
                        else:
                            num_value = random.choice([2, 3])
                        #
                        if num_value == 2:
                            value_pos = random.randrange(len(con_variables[k]))
                            value_pos2 = random.randrange(len(con_variables[k]))
                            while value_pos == value_pos2:
                                value_pos2 = random.randrange(len(con_variables[k]))
                            constraints.append((con_variables[i][j] + ',' + operator2[0] + con_variables[k][value_pos] +
                                                re.sub(r'.*,', ',', con_variables[k][value_pos2]) + '\n').replace(" ", ""))
                        else:
                            choices = [0, 1, 2, 3]
                            value_pos = random.choice(choices)
                            choices.remove(value_pos)
                            value_pos2 = random.choice(choices)
                            choices.remove(value_pos2)
                            value_pos3 = random.choice(choices)
                            constraints.append((con_variables[i][j] + ',' + operator2[0] + con_variables[k][value_pos] +
                                               re.sub(r'.*,', ',', con_variables[k][value_pos2]) +
                                               re.sub(r'.*,', ',', con_variables[k][value_pos3]) + '\n').replace(" ", ""))

    #
    with open('KnowledgeBase' + kb_no + 'Constraints.txt', 'w') as file:
        file.writelines(constraints)
    return save_operators


variables = 39  # 0 to given number
operator_list = []
knowledge_base1, knowledge_base2 = createKnowledgeBases(variables)
operator_list = createConstraints(knowledge_base1, "1", operator_list)
createConstraints(knowledge_base2, "2", operator_list)
