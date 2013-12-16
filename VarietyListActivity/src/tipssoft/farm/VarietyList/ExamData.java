package tipssoft.farm.VarietyList;

public class ExamData
{
    public byte type = 0;
    public String data1 = null;
    public String data2 = null;
    
    public ExamData(byte parm_type, String parm_data1, String parm_data2)
    {
        type = parm_type;
        data1 = parm_data1;
        data2 = parm_data2;
    }
}