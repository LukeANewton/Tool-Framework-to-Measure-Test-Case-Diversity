package data_representation;

import core.InvalidFormatException;

import java.util.ArrayList;
import java.util.List;

/**
 * this is a data representation for a format used in the case studies.
 * This format starts with an id enclosed in square brackets, and then
 * is a dash-separated list of states and events:
 *
 *[<id>] Start-<event>-<state>-<event>...
 *
 * this particular data representation reads that format in, and stores
 * the events and resulting states as pairs
 *
 * @author luke
 */
public class EventStatePairs implements DataRepresentation {
    List<String> events;
    Long id;
    int iteratorIndex;

    /**Constructor*/
    public EventStatePairs(){
        events = new ArrayList<>();
        id = null;
        iteratorIndex = 0;
    }

    /**Constructor*/
    public EventStatePairs(String s) throws InvalidFormatException {
        events = new ArrayList<>();
        id = null;
        iteratorIndex = 0;
        this.parse(s);
    }

    @Override
    public boolean hasNext() {
        return events.size() > iteratorIndex;
    }

    @Override
    public Object next() {
        return (hasNext()) ? events.get(iteratorIndex++) : null;
    }

    @Override
    public void parse(String s) throws InvalidFormatException {
        if(s == null || s.length() == 0)
            throw new InvalidFormatException("empty string");

        if (s.charAt(0) == '['){//there is an id at the start of the test case
            if (s.charAt(1) == ']')
                throw new InvalidFormatException("no number inside identifier brackets");
            int i = 2;
            for(;s.charAt(i) != ']';i++){}//find the end of the id
            try {
                id = Long.valueOf(s.substring(1, i));
                s = s.substring(i+1).trim(); //remove the identifier from the test case string
            }catch(NumberFormatException e) {
                throw new InvalidFormatException("contents between square brackets is not a number");
            }
        }

        if(!s.startsWith("Start-"))
            throw new InvalidFormatException("sequence does not begin with Start state");
        s = s.substring(6);

        String[] stateEventList = s.split("-");
        if(stateEventList.length == 1 && stateEventList[0].equals(""))
            throw new InvalidFormatException("no elements in the list");
        for(int i = 0; i < stateEventList.length; i+=2) {
            try {
                events.add("(" + stateEventList[i] + "," + stateEventList[i + 1] + ")");
            }catch(IndexOutOfBoundsException e){
                throw new InvalidFormatException("final event does not have a following state");
            }
        }
    }

    @Override
    public boolean checkFormat(String s) {
        if(s == null || s.length() == 0)
            return false;
        if (s.charAt(0) == '['){//there is an id at the start of the test case
            if (s.charAt(1) == ']')
                return false;
            int i = 2;
            for(;s.charAt(i) != ']';i++){}//find the end of the id
            try {
                Long.valueOf(s.substring(1, i));
                s = s.substring(i+1).trim(); //remove the identifier from the test case string
            }catch(NumberFormatException e) {
                return false;
            }
        }

        if(!s.startsWith("Start-"))
            return false;
        s = s.substring(6);

        String[] stateEventList = s.split("-");
        if(stateEventList.length == 1 && stateEventList[0].equals(""))
            return false;
        //every event should have a following state, so length must be even
        return stateEventList.length % 2 != 1;
    }

    @Override
    public String getDescription() {
        return "reads in a dash-separated list of alternating events and states, storing each event and resulting state as a tuple";
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        if(id != null)
            s.append('[').append(id).append("] ");
        s.append(events.get(0));
        for(int i = 1; i < events.size(); i++)
            s.append('-').append(events.get(i));

        return s.toString();
    }
}
