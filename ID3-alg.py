import math
import random
import time
import sys


class Node(object):
    """This is a node of a tree. Initialize it and then add children"""
    def __init__(self, name, classification=None):
        super(Node, self).__init__()
        self.name = name
        self.classification = classification
        self.children = []

    def addChild(self, node):
        self.children.append(node)

    def __str__(self):
        return '{} {} {}'.\
            format(self.name, str(self.children), self.classification)


class Feature(object):
    """This is a feature with a name (type) and value"""
    def __init__(self, name, value):
        super(Feature, self).__init__()
        self.name = name
        self.value = value

    def __str__(self):
        "{} - {}".format(self.name, self.value)


class FeatureType(object):
    """stores the string name and list of valid values for the feature"""
    values = []

    def __init__(self, name, values):
        super(FeatureType, self).__init__()
        self.name = name
        self.values = values

    def __str__(self):
        return '{} - {}'.format(self.name, self.values)


class Datum(object):
    """This is a vector of features, with a classification assigned to it"""
    def __init__(self, features, classification=None):
        super(Datum, self).__init__()
        self.features = features
        self.classification = classification

    def __str__(self):
        return '{} - {}'.format(self.classification, self.features)


class Test(object):
    """Test that is run with the algorithm, storing the
    number of successes (and thus errors)"""
    def __init__(self, numRuns, numSuccess):
        super(Test, self).__init__()
        self.runs = numRuns
        self.successes = numSuccess
        self.errors = numRuns - numSuccess
        self.percentage = self.successes / self.runs


def entropy(dataSet):
    theSum = 0

    numEachTypeCount = dict()

    for data in dataSet:
        if data.classification in numEachTypeCount:
            numEachTypeCount[data.classification] = \
                numEachTypeCount[data.classification] + 1
        else:
            numEachTypeCount[data.classification] = 1

    #print numEachTypeCount
    sizeOfSet = float(len(dataSet))

    for theType in numEachTypeCount:
        probabilityOfClass = numEachTypeCount[theType] / sizeOfSet
        #print probabilityOfClass
        theSum += probabilityOfClass * math.log(probabilityOfClass, 2)

    theSum = -theSum

    #print theSum
    return theSum


def infoGain(dataSet, theType):
    origSize = float(len(dataSet))
    startEntropy = entropy(dataSet)

    toSearch = theType.name
    tempSum = 0

    for value in theType.values:
        newSet = []
        for datum in dataSet:
            if datum.features[datumFeatureLocation[toSearch]] == value:
                newSet.append(datum)
        tempSum += (float(len(newSet)) / origSize) * entropy(newSet)
    theGain = startEntropy - tempSum
    return theGain


def splitInfoGain(dataSet, theType):
    featureType = getFeatureType(theType)
    theSum = 0
    for x in featureType.values:
        newDataSet = getDataSetWithSetFeatureValue(dataSet, featureType, x)
        probability = float(len(newDataSet)) / float(len(dataSet))
        if probability == 0:
            return 1
        else:
            theSum += probability * math.log(probability, 2)

    theSum = -theSum
    return theSum


def infoGainMisclass(dataSet, theType):
    origSize = float(len(dataSet))

    startEntropy = misclassificationRate(dataSet)

    toSearch = theType.name
    tempSum = 0

    for value in theType.values:
        newSet = []
        for datum in dataSet:
            if datum.features[datumFeatureLocation[toSearch]] == value:
                newSet.append(datum)
        tempSum += (float(len(newSet)) / origSize) \
            * misclassificationRate(newSet)

    theGain = startEntropy - tempSum
    if (math.fabs(theGain) < 0.0000001):
        theGain = 0
    return theGain


def misclassificationRate(dataSet):
    origLength = len(dataSet)
    if origLength == 0:
        return 0

    theMax = 0
    for c in possibleClassValues:
        value = float(len(getSetWithClassification(dataSet, c))) / origLength
        if value >= theMax:
            theMax = value
    theMax = 1 - theMax
    return theMax


def getSetWithClassification(dataSet, classification):
    retSet = []
    for x in dataSet:
        if x.classification == classification:
            retSet.append(x)
    return retSet


def id3(dataSet, featureSet):
    maxValue = 0
    maxFeature = None

    #print "feature set for id3"
    #for x in featureSet:
    #    print x
    #print "number of items in dataset: " + str(len(dataSet))
    startEntropyMisclass = 0
    global gainType
    if gainType == "1" or gainType == "2":
        startEntropyMisclass = entropy(dataSet)
    else:
        startEntropyMisclass = misclassificationRate(dataSet)

    if len(featureSet) == 1 or startEntropyMisclass == 0:
        #print "reached leaf node, pick a classification"
        #for x in dataSet:
        #    print x.classification
        return getModeClassType(dataSet)

    #for each feature in the feature set
    for feature in featureSet:
        global gainType
        if gainType == "1":
            # calculate the info gain from using this feature as the root
            value = infoGain(dataSet, feature)
            #print "Info gain: " + str(value)
        elif gainType == "2":
            #print "doing split info"
            value = float(infoGain(dataSet, feature)) / \
                splitInfoGain(dataSet, feature)
            #print "SplitInfo gain: " + str(value)
        elif gainType == "3":
            #print "info gain using misclassification as entropy"
            value = infoGainMisclass(dataSet, feature)
            #print "SplitInfo gain: " + str(value)
        else:
            print "Not a valid gainType???"

        if value >= maxValue:
            maxValue = value
            maxFeature = feature

    if maxFeature is None:
        print "WTF: ID3 shows none for max feature!!!"

    curNode = Node(maxFeature.name)
    #print "Picked node: " + str(curNode)
    for possValue in maxFeature.values:
        newFeatures = featureSet[:]
        newFeatures.remove(maxFeature)
        #print "printing data set before adding new child"
        newDataSet = \
            getDataSetWithSetFeatureValue(dataSet[:], maxFeature, possValue)
        #for x in newDataSet:
        #    print x
        if len(newDataSet) > 0:
            curNode.addChild(id3(newDataSet, newFeatures))
        else:
            curNode.addChild(Node("leaf", "N/A"))
    return curNode


def getDataSetWithSetFeatureValue(dataSet, feature, value):
    location = datumFeatureLocation[feature.name]
    newDataSet = []
    for datum in dataSet:
        if datum.features[location] == value:
            newDataSet.append(datum)
    return newDataSet


def getModeClassType(dataSet):
    maxCount = dict()
    for x in dataSet:
        if x.classification in maxCount:
            maxCount[x.classification] = maxCount[x.classification] + 1
        else:
            maxCount[x.classification] = 1

    maxValue = 0
    maxClass = None
    for k, v in maxCount.iteritems():
        if v >= maxValue:
            maxClass = k
    if maxClass is None:
        print "WTF: maxClass = none"
        print maxCount.items()
        print len(dataSet)
        quit()
    return Node("leaf", maxClass)


def getFeatureType(feature):
    for x in featureTypeList:
        if (x.name == feature.name):
            return x


def getFeatureTypeByName(name):
    for x in featureTypeList:
        if x.name == name:
            return x


def permuteData(dataSet):
    print "PERMUTING DATA " + str(len(dataSet)) + "  "
    for i in range(len(dataSet) - 1, 1, -1):
        j = random.randint(0, i)
        #print 'random num: ' + str(j)
        temp = dataSet[i]
        dataSet[i] = dataSet[j]
        dataSet[j] = temp
    return dataSet


def splitList(theList, n):
    return [theList[i:i + (len(theList) / n)] for i
            in range(0, len(theList), len(theList) / n)]


def readConfigFile(filename):
    #featureTypeList = []
    f = open(filename, 'r')
    global gainType
    gainType = f.readline().rstrip()
    print "Gain type = " + gainType
    global possibleClassValues
    possibleClassValues = f.readline().rstrip().split(',')
    print "possible classification values: " + str(possibleClassValues)
    i = 0
    for line in f:
        lineList = line.rstrip().split(',')
        datumFeatureLocation[lineList[0]] = i
        i = i + 1
        featureTypeList.append(FeatureType(lineList[0], lineList[1:]))

    f.close()


def readDataFile(filename):
    dataSet = []
    f = open(filename, 'r')
    for line in f:
        lineList = line.lstrip().rstrip().split(',')
        dataSet.append(Datum(lineList[1:], lineList[0]))

    f.close()

    return dataSet


def classify(node, datum):
    if node is None:
        print "DON'T KNOW"
        print "Should be : " + datum.classification
        quit()
        return None
    if node.classification == "N/A":
        return None
    if node.classification is not None:
        return node.classification
    else:
        feature = node.name
        featureIndex = datumFeatureLocation[feature]
        valueOfDatumFeature = datum.features[featureIndex]
        possibleValuesList = getFeatureTypeByName(feature).values

        if possibleValuesList is None:
            print "ERRORRORROROOR"
            quit()

        nextNodeToChoose = possibleValuesList.index(valueOfDatumFeature)
        #print "going to next node due to value of " + feature + " = "\
        # + valueOfDatumFeature
        return classify(node.children[nextNodeToChoose], datum)


def printTree(root, iteration=0, value=None):
    retVal = ""
    if root.name == "leaf":
        return str("\t") + root.classification

    i = 0

    for x in root.children:
        retVal = retVal + "\n" + str("\t" * iteration) + root.name + \
            " = " + getFeatureType(root).values[i]

        retVal = retVal + printTree(x, iteration + 1, value)

        i = i + 1
    return retVal


### calculates whether or not 2 tests are similar,
def testForDifference(test1, test2):
    if len(test1) != len(test2):
        return None

    difference = []
    for i in xrange(0, len(test1)):
        difference.append(test1[i] - test2[i])

    dAvg = getMean(difference)
    #print "sample mean difference = " + str(dAvg)

    s = math.sqrt(calculateSampleVariance(difference))
    #print "sample std = " + str(s)

    tval = ttable[len(test1) - 1]

    dist = tval * (float(s) / math.sqrt(len(test1)))
    #interval = [dAvg - dist, dAvg + dist]
    #print "dist = " + str(dist) + "    interval : " + str(interval)

    if dAvg - dist <= 0 and dAvg + dist >= 0:
        return "same"
    else:
        return "different"


def getMean(data):
    if len(data) == 0:
        return None
    theSum = 0
    for x in data:
        theSum += x
    return theSum / len(data)


### calculate the variance of a set of numbers
def calculateSampleVariance(data):
    num = len(data)
    if (num == 0):
        return 0

    mean = getMean(data)

    theSum = 0
    for x in data:
        theSum = theSum + ((x - mean) * (x - mean))
    variance = theSum / (num - 1)
    return variance


def confidenceInterval(errors, numTests, score, sigma):
    ### score = t or z table score for this interval
    percentage = float(errors) / numTests
    distance = score * sigma
    return (percentage - distance, percentage + distance)


def help():
    string = """
    (C) Shawn Hanna 2013
    This program runs the id3 classification algorithm using several types of
    gain functions.
    you can specify the type of gain function inside the config file

    usage:
    python cs1573-hw.py configFileName trainFileName testFileName"""
    return string


if len(sys.argv) != 4:
    print help()
    quit(10)


random.seed(time.time())

### 0.95 confidence
ttable = [0, 12.71, 4.303, 3.182, 2.776, 2.571, 2.447, 2.365,
          2.306, 2.262, 2.228, 2.201]

### Possible values for classification
### 0 = false, 1 = true, or could be a different discrete set of values
possibleClassValues = []

### List of all feature types
featureTypeList = []

###this is a map from string to integer in the list of the datum
datumFeatureLocation = dict()

######################################################
### read configuration file
gainTypeList = ["1", "2", "3"]
global gainType
gainType = "1"
readConfigFile(sys.argv[1])

### read test file
dataSet = readDataFile(sys.argv[3])
######################################################

dataSet = permuteData(dataSet)

numFolds = 10

chunkSet = splitList(dataSet, numFolds)

percentageList = []

for gType in range(len(gainTypeList)):
    percentageList.append([])
    for i in range(0, numFolds):
        testDataSet = chunkSet[i]
        #trainDataSet = readDataFile("monks-3.train")

        trainDataSet = []
        for k in range(0, i):
            for data in chunkSet[k]:
                trainDataSet.append(data)
        for k in range(i + 1, numFolds):
            for data in chunkSet[k]:
                trainDataSet.append(data)

        global gainType
        gainType = gainTypeList[gType]
        root = id3(trainDataSet[:], featureTypeList[:])

        numWrong = 0
        numNotAbleToClassify = 0
        for datum in trainDataSet:
            classification = classify(root, datum)
            if classification is None:
                numNotAbleToClassify = numNotAbleToClassify + 1
            if classification != datum.classification:
                numWrong = numWrong + 1
        percentWrong = float(numWrong) / len(trainDataSet)
        numCorrect = len(trainDataSet) - numWrong

        print "The accuracy on the training data is: " + str(numCorrect) + \
            "/" + str(len(trainDataSet)) + " = " + str(str(1 - percentWrong)) \
            + "%"

        numWrong = 0
        numNotAbleToClassify = 0
        for datum in testDataSet:
            classification = classify(root, datum)
            if classification is None:
                numNotAbleToClassify = numNotAbleToClassify + 1
            if classification != datum.classification:
                numWrong = numWrong + 1
        percentWrong = float(numWrong) / len(testDataSet)
        numCorrect = len(testDataSet) - numWrong

        print "The accuracy on the test data is: " + str(numCorrect) + \
            "/" + str(len(testDataSet)) + " = " + str(str(1 - percentWrong)) \
            + "%"
        percentageList[gType].append(percentWrong)

        print "The final Decision tree:"
        print printTree(root)

        print "##########################################"

print "Starting statistical analysis"

output = " "
for t in gainTypeList:
    output += t + "\t  "
print output
for count in range(len(percentageList[0])):
    output = ""
    for p in range(len(percentageList)):
        string = '%(num)03f' % {"num": percentageList[p][count]}
        output += " " + string
    print output

for i in range(len(percentageList)):
    for j in range(i, len(percentageList)):
        if i != j:
            print "comparing algorithm: " + str(i) + " and " + str(j) + \
                " = " + testForDifference(percentageList[i], percentageList[j])
